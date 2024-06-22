package com.swp391.jewelrysalesystem.services;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class GenericService<T> implements IGenericService<T> {

    @Override
    public T getByField(String value, String field, String collection, Class<T> clazz) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference collectionReference = dbFirestore.collection(collection);

        Query query = collectionReference.whereEqualTo(field, value);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
        if (!documents.isEmpty()) {
            return documents.get(0).toObject(clazz);
        }
        return null;
    }

    @Override
    public T getByField(int value, String field, String collection, Class<T> clazz) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference collectionReference = dbFirestore.collection(collection);

        Query query = collectionReference.whereEqualTo(field, value);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
        if (!documents.isEmpty()) {
            return documents.get(0).toObject(clazz);
        }
        return null;
    }

    @Override
    public List<T> getListByField(String value, String field, String collection, Class<T> clazz)
            throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference collectionReference = dbFirestore.collection(collection);


        Query query = collectionReference.whereEqualTo(field, value);

        // Retrieve query results
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        try {
            return querySnapshot.get().toObjects(clazz);
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error retrieving "+ collection + " by " + field + ": " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Unexpected error occurred while retrieving " + collection, e);
        }
    }

    @Override
    public List<T> getListByField(int value, String field, String collection, Class<T> clazz)
            throws InterruptedException, ExecutionException {
                Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference collectionReference = dbFirestore.collection(collection);


        Query query = collectionReference.whereEqualTo(field, value);

        // Retrieve query results
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        try {
            return querySnapshot.get().toObjects(clazz);
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error retrieving "+ collection + " by " + field + ": " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Unexpected error occurred while retrieving " + collection, e);
        }
    }

    
    @Override
    public List<T> getList(String collection, Class<T> clazz) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference collectionReference = dbFirestore.collection(collection);

        // Retrieve query results
        ApiFuture<QuerySnapshot> querySnapshot = collectionReference.get();

        try {
            return querySnapshot.get().toObjects(clazz);
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error retrieving "+ collection + " :" + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Unexpected error occurred while retrieving " + collection, e);
        }
    }

    @Override
    public boolean saveObject(T object, String collection, int documentID) {
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            DocumentReference documentReference = dbFirestore.collection(collection).document(String.valueOf(documentID));
            ApiFuture<WriteResult> future = documentReference.set(object);
            future.get();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteObject(int documentID, String collection) {
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            DocumentReference documentReference = dbFirestore.collection(collection).document(String.valueOf(documentID));
            ApiFuture<WriteResult> future = documentReference.delete();
            future.get();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean changeStatus(int ID, String collection, Class<T> clazz) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference docRef = dbFirestore.collection(collection).document(String.valueOf(ID));

        try {
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();

            if (document.exists()) {
                T object = document.toObject(clazz);
                if (object != null && object instanceof StatusUpdatable) {
                    StatusUpdatable statusUpdatable = (StatusUpdatable) object;
                    statusUpdatable.setStatus(!statusUpdatable.getStatus());
                    docRef.set(object);
                    return true;
                }
            }
            return false;
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error retrieving document: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}
