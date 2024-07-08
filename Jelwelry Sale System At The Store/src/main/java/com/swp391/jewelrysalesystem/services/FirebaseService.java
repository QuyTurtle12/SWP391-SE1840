package com.swp391.jewelrysalesystem.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@DependsOn("firebaseConfig") // Ensure FirebaseConfig is initialized first
public class FirebaseService {

    private Firestore firestore;

    @PostConstruct
    public void init() {
        this.firestore = FirestoreClient.getFirestore();
    }

    public void addNewEntry(String collectionName, Map<String, Object> newEntryData) {
        CollectionReference collectionReference = firestore.collection(collectionName);

        // Fetch the highest existing ID
        ApiFuture<QuerySnapshot> future = collectionReference.orderBy("id", Query.Direction.DESCENDING).limit(1).get();
        int newId = 1; // Default to 1 if no entries exist

        try {
            QuerySnapshot querySnapshot = future.get();
            if (!querySnapshot.isEmpty()) {
                for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                    newId = document.getLong("id").intValue() + 1; // Increment the highest existing ID
                    break;
                }
            }

            // Add the new entry with the incremented ID
            newEntryData.put("id", newId);
            ApiFuture<com.google.cloud.firestore.WriteResult> writeFuture = collectionReference
                    .document(String.valueOf(newId)).set(newEntryData);
            writeFuture.get(); // Wait for the write operation to complete

            System.out.println("New entry added successfully with ID: " + newId);
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error adding new entry: " + e.getMessage());
        }
    }
}
