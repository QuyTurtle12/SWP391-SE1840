package com.swp391.jewelrysalesystem.services;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.cloud.FirestoreClient;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import com.swp391.jewelrysalesystem.models.User;;

@Service
public class UserService implements IUserService {
    private String uid = null;

    public String login(String idToken) throws  FirebaseAuthException, InterruptedException, ExecutionException{
        FirebaseToken dedcodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
        String uid = dedcodedToken.getUid();
        this.uid = uid;
        return uid;
    }

    //Get user data base on uid
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
}
