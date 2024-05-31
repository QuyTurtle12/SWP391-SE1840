package com.swp391.jewelrysalesystem.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.swp391.jewelrysalesystem.models.Counter;
import com.swp391.jewelrysalesystem.models.Promotion;

@Service
public class CounterService implements ICounterService {

    @Override
    public Counter saveCounter(Counter counter) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("counter")
                .document(String.valueOf(counter.getID()));

        ApiFuture<WriteResult> future = documentReference.set(counter);
        try {
            future.get();
            return counter;
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error saving counter document: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void removeCounter(int ID) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("counter").document(String.valueOf(ID));

        ApiFuture<WriteResult> future = documentReference.delete();
        try {
            future.get();
            System.out.println("Counter with ID " + ID + " has been deleted.");
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error deleting counter document: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Counter> getCountersList() {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference counterCollection = dbFirestore.collection("counter");

        try {
            ApiFuture<QuerySnapshot> future = counterCollection.get();
            QuerySnapshot counters = future.get();

            List<Counter> counterList = new ArrayList<>();
            for (QueryDocumentSnapshot document : counters) {
                counterList.add(document.toObject(Counter.class));
            }
            return counterList;
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error retrieving counter list: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Counter getCounter(int ID) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference counterRef = dbFirestore.collection("counter").document(String.valueOf(ID));

        try {
            ApiFuture<DocumentSnapshot> future = counterRef.get();
            DocumentSnapshot document = future.get();

            if (document.exists()) {
                Counter counter = document.toObject(Counter.class);
                return counter;
            }
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error retrieving counter: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Counter changeStatus(int ID) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference counterRef = dbFirestore.collection("counter").document(String.valueOf(ID));

        try {
            ApiFuture<DocumentSnapshot> future = counterRef.get();
            DocumentSnapshot document = future.get();

            if (document.exists()) {
                Counter counter = document.toObject(Counter.class);
                if (counter != null) {
                    counter.setStatus(!counter.getStatus());
                    counterRef.set(counter);
                    return counter;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error retrieving counter: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
}
