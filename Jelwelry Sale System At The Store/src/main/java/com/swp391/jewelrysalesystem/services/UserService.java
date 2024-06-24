package com.swp391.jewelrysalesystem.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.firebase.cloud.FirestoreClient;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import com.swp391.jewelrysalesystem.models.User;

@Service
public class UserService implements IUserService {

    @Autowired
    private PasswordService passwordService;

    // @Autowired
    // public FirebaseService firebaseService;

    // Method to get a user by email
    public User getUserByEmail(String email) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference users = dbFirestore.collection("user");

        // Create a query to get the user with the specified email
        ApiFuture<QuerySnapshot> querySnapshot = users.whereEqualTo("email", email).get();

        List<User> userList = querySnapshot.get().toObjects(User.class);

        if (userList.isEmpty()) {
            System.out.println("No user found with email " + email);
            return null;
        } else {
            return userList.get(0);
        }
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

    public String getUserList() {
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            QuerySnapshot querySnapshot = dbFirestore.collection("user").get().get();
            List<String> documents = querySnapshot.getDocuments().stream()
                    .map(QueryDocumentSnapshot::getData)
                    .map(Object::toString)
                    .collect(Collectors.toList());
            return documents.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    @Override
    public User getUserByUserID(int userID) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference users = dbFirestore.collection("user");

        // Create a query to get a specific user ID
        Query query = users.whereEqualTo("id", userID);

        // Retrieve query results
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        List<User> userList = querySnapshot.get().toObjects(User.class);

        if (userList.isEmpty()) {
            System.out.println("User document with userID" + userID + "does not exist");
            return null;
        } else {
            return userList.get(0);
        }
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
    public List<User> getUserByUserRole(String role) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference users = dbFirestore.collection("user");

        int roleID = 0;
        switch (role) {
            case "MANAGER":
                roleID = 2;
                break;
            case "STAFF":
                roleID = 1;
                break;
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }

        // Create a query to get users who have role that you want
        Query query = users.whereEqualTo("roleID", roleID);

        // Retrieve query results
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        try {
            return querySnapshot.get().toObjects(User.class);
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error retrieving users by role: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            // Log unexpected exceptions
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Unexpected error occurred while retrieving users by role", e);
        }

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
            return getUserByUserID(ID) != null ? true : false;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isNotExistedPhoneNum(int ID, String contactInfo) {
        try {
            User firstUser = getUserByUserID(ID);
            User secondUser = getUserByUserPhone(contactInfo);

            if (firstUser == null || secondUser == null) {
                return true;
            }
            if (firstUser.getID() == secondUser.getID()) {
                return true;
            }
            return false;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User getUserByUserPhone(String contactInfo) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference users = dbFirestore.collection("user");

        Query query = users.whereEqualTo("contactInfo", contactInfo);

        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        try {
            List<User> userList = querySnapshot.get().toObjects(User.class);
            if (!userList.isEmpty()) {
                return userList.get(0);
            } else {
                return null; // or throw an exception if you want to handle no user found scenario
            }
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error retrieving users by role: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Unexpected error occurred while retrieving users by contactInfo", e);
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

    // Function to register a new user with hashed password
    /*
     * public boolean registerUser(User user) {
     * Firestore dbFirestore = FirestoreClient.getFirestore();
     * user.setPassword(passwordService.hashPassword(user.getPassword()));
     * DocumentReference documentReference =
     * dbFirestore.collection("user").document(String.valueOf(user.getID()));
     * 
     * try {
     * // First, set the user document
     * ApiFuture<com.google.cloud.firestore.WriteResult> future =
     * documentReference.set(user);
     * future.get(); // Wait for the write operation to complete
     * 
     * // Then, add the new entry with auto-increment ID
     * String collectionName = "user";
     * firebaseService.addNewEntry(collectionName, user.toMap());
     * 
     * return true;
     * } catch (Exception e) {
     * System.err.println("Error saving user document: " + e.getMessage());
     * e.printStackTrace();
     * return false;
     * }
     * }
     */

}
