package com.swp391.jewelrysalesystem.services;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.cloud.FirestoreClient;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import com.swp391.jewelrysalesystem.models.User;;

@Service
public class UserService implements IUserService {
    private String uid = null;

    public String login(String idToken) throws FirebaseAuthException, InterruptedException, ExecutionException {
        FirebaseToken dedcodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
        String uid = dedcodedToken.getUid();
        this.uid = uid;
        return uid;
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

        //Create a query to get a specific user ID
        Query query = users.whereEqualTo("id", userID);

        //Retrieve query results
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
    public User saveUser(User user) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("user").document(String.valueOf(user.getID()));

        ApiFuture<com.google.cloud.firestore.WriteResult> future = documentReference.set(user);
        try {
            future.get();
            return user;
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error saving user document: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User changeUserStatus(int ID) {
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
                    return user;
                }
            } else {
                System.out.println("User document with ID " + ID + " does not exist.");
            }
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error changing user status: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
    
    @Override
    public List<User> getUserByUserRole(String role) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference users = dbFirestore.collection("user");

        //Create a query to get users who have role that you want
        Query query = users.whereEqualTo("role", role);

        //Retrieve query results
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        try {
            return querySnapshot.get().toObjects(User.class);
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error retrieving users by role: " + e.getMessage());
            throw e;  
        } catch (Exception e){
            // Log unexpected exceptions
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Unexpected error occurred while retrieving users by role", e);
        }
        
    }
}
