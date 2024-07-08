package com.swp391.jewelrysalesystem.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.google.firebase.cloud.FirestoreClient;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.swp391.jewelrysalesystem.models.User;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    @Autowired
    private PasswordService passwordService;
    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());
    private IGenericService<User> genericService;
    private ICounterService counterService;

    @Autowired
    public UserService(IGenericService<User> genericService, ICounterService counterService) {
        this.genericService = genericService;
        this.counterService = counterService;
    }

    // Method to get a user by email
    public User getUserByEmail(String email) throws InterruptedException, ExecutionException {
        return genericService.getByField(email, "email", "user", User.class);
    }

    public User getUserByEmailAndPassword(String email, String rawPassword)
            throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference users = dbFirestore.collection("user");

        // Create a query to get the user with the specified Email
        Query query = users.whereEqualTo("email", email);

        // Retrieve query results
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<User> userList = querySnapshot.get().toObjects(User.class);

        if (userList.isEmpty()) {
            System.out.println("No user found with email " + email);
            return null;
        } else {
            User user = userList.get(0);
            if (passwordService.verifyPassword(rawPassword, user.getPassword())) {
                return user;
            } else {
                System.out.println("Invalid password for email " + email);
                return null;
            }
        }
    }

    public List<GrantedAuthority> getAuthorities(int roleID) {
        return List.of(roleID).stream()
                .map(role -> {
                    switch (role) {
                        case 1:
                            return new SimpleGrantedAuthority("ROLE_STAFF");
                        case 2:
                            return new SimpleGrantedAuthority("ROLE_MANAGER");
                        case 3:
                            return new SimpleGrantedAuthority("ROLE_ADMIN");
                        default:
                            throw new IllegalArgumentException("Invalid role ID: " + role);
                    }
                })
                .collect(Collectors.toList());
    }

    // Get user data base on uid
    public User getUserData(String userId) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("user").document(userId);

        try {
            ApiFuture<DocumentSnapshot> future = documentReference.get();
            DocumentSnapshot document = future.get();

            if (document.exists()) {
                return document.toObject(User.class);
            } else {
                System.out.println("User document with ID " + userId + " does not exist.");
            }
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error retrieving user document: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public boolean saveUser(User user) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        // user.setPassword(passwordService.hashPassword(user.getPassword()));
        DocumentReference documentReference = dbFirestore.collection("user").document(String.valueOf(user.getID()));

        ApiFuture<com.google.cloud.firestore.WriteResult> future = documentReference.set(user);
        try {
            future.get();
            return true;
        } catch (Exception e) {
            System.err.println("Error saving user document: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean changeUserStatus(int ID) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("user").document(String.valueOf(ID));

        try {
            ApiFuture<DocumentSnapshot> future = documentReference.get();
            DocumentSnapshot document = future.get();

            if (document.exists()) {
                User user = document.toObject(User.class);
                if (user != null) {
                    user.setStatus(!user.getStatus()); // Toggle status
                    documentReference.set(user); // Update the user document
                    return true;
                }
            } else {
                System.out.println("User document with ID " + ID + " does not exist.");
                return false;
            }
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error changing user status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public List<User> searchUser(String input, String filter, List<User> userList) {

        List<User> newUserList = new ArrayList<>();
        switch (filter) {
            case "ByName":
                for (User user : userList) {
                    if (user.getFullName().toLowerCase().trim().contains(input.toLowerCase())) {
                        newUserList.add(user);
                    }
                }

                break;
            case "ByCounterID":
                for (User user : userList) {
                    if (user.getCounterID() == Integer.parseInt(input)) {
                        newUserList.add(user);
                    }
                }

                break;
            case "ByStatus":
                for (User user : userList) {
                    if (user.getStatus().toString().toLowerCase().equals(input.toLowerCase())) {
                        newUserList.add(user);
                    }
                }

                break;

            case "ByPhoneNum":
                for (User user : userList) {
                    if (user.getContactInfo().toLowerCase().trim().contains(input.toLowerCase())) {
                        newUserList.add(user);
                    }
                }
            default:
                throw new IllegalArgumentException("Invalid filter: " + filter);
        }
        return newUserList;
    }

    @Override
    public boolean isNotNullUser(int ID) {
        try {
            return getUserByField(ID, "id", "user") != null ? true : false;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isNotExistedPhoneNum(String contactInfo) {
        try {
            User existedPhoneNum = getUserByField(contactInfo, "contactInfo", "user");

            if (existedPhoneNum == null) {
                return true;
            }

            return false;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Function to register a new user with hashed password
    public boolean registerUser(User user) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        user.setPassword(passwordService.hashPassword(user.getPassword()));
        DocumentReference documentReference = dbFirestore.collection("user").document(String.valueOf(user.getID()));

        ApiFuture<com.google.cloud.firestore.WriteResult> future = documentReference.set(user);
        try {
            future.get();
            return true;
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error saving user document: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isMyPhoneNum(int ID, String contactInfo) {
        try {
            User userWithThisPhoneNum = getUserByField(contactInfo, "contactInfo", "user");
            User user = getUserByField(ID, "id", "user");

            if (user == null && userWithThisPhoneNum != null) {
                return false;
            }

            if (user != null && userWithThisPhoneNum != null) {
                if (user.getID() == userWithThisPhoneNum.getID()) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean isNotExistedEmail(String email) {
        try {
            User userWithThisEmail = getUserByField(email, "email", "user");

            if (userWithThisEmail == null) {
                return true;
            }

            return false;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isMyEmail(int ID, String email) {
        try {
            User userWithThisEmail = getUserByField(email, "email", "user");
            User user = getUserByField(ID, "id", "user");

            if (user == null && userWithThisEmail != null) {
                return false;
            }

            if (user != null && userWithThisEmail != null) {
                if (user.getID() == userWithThisEmail.getID()) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public User getUserByField(String value, String field, String collection)
            throws InterruptedException, ExecutionException {
        return genericService.getByField(value, field, collection, User.class);
    }

    @Override
    public User getUserByField(int value, String field, String collection)
            throws InterruptedException, ExecutionException {
        return genericService.getByField(value, field, collection, User.class);
    }

    @Override
    public List<User> getUserListByField(String value, String field, String collection)
            throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference users = dbFirestore.collection(collection);

        Query query = null;
        if (field.equals("roleID")) {
            int roleID = getRoleID(value);
            query = users.whereEqualTo(field, roleID);
        } else {
            query = users.whereEqualTo(field, value);
        }

        // Retrieve query results
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        try {
            return querySnapshot.get().toObjects(User.class);
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error retrieving users by: " + field + ": " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Unexpected error occurred while retrieving users", e);
        }
    }

    public int getRoleID(String roleName) {
        switch (roleName) {
            case "ADMIN":
                return 3;
            case "MANAGER":
                return 2;
            case "STAFF":
                return 1;
            default:
                throw new IllegalArgumentException("Invalid role: " + roleName);
        }
    }

    @Override
    public List<User> getUserListByField(int value, String field, String collection)
            throws InterruptedException, ExecutionException {
        return genericService.getListByField(value, field, collection, User.class);
    }

    @Override
    public String isGeneralValidated(String fullName, String gender, String contactInfo, int counterID) {

        if (fullName.isBlank() || fullName.equals(null)) {
            return "Full name cannot be empty!";
        }

        if (!gender.equals("Male") && !gender.equals("Female") && !gender.equals("Other")) {
            return "Incorrect gender format.";
        }

        if (contactInfo.isBlank() || contactInfo.equals(null)) {
            return "Contact Info cannot be empty!";
        }

        if (!contactInfo.matches("0\\d+")) {
            return "Contact Info must contain only numeric characters and start with zero!";
        }

        if (contactInfo.length() < 10 || contactInfo.length() > 11) {
            return "Phone Number must be in range 10 - 11";
        }

        if (!counterService.isNotNullCounter(counterID)) {
            return "Invalid counter ID";
        }
        return null;
    }

    @Override
    public int generateID() {
        return genericService.generateID("user", User.class, User::getID);
    }
    public boolean checkIfEmailExists(String email) {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> query = db.collection("user").whereEqualTo("email", email).get();
        try {
            QuerySnapshot querySnapshot = query.get();
            boolean exists = !querySnapshot.isEmpty();
            LOGGER.info("Email check for " + email + ": " + exists);
            return exists;
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.severe("Error checking email existence: " + e.getMessage());
            return false;
        }
    }

}
