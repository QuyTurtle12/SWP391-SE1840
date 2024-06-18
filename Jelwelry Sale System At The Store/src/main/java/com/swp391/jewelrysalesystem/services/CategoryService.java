package com.swp391.jewelrysalesystem.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.swp391.jewelrysalesystem.models.Category;

@Service
public class CategoryService implements ICategory{

    @Override
    public boolean saveCatogory(Category category) {
        try {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference categoryRef = dbFirestore.collection("category").document(String.valueOf(category.getCategoryID()));
        ApiFuture<WriteResult> future = categoryRef.set(category);
        future.get();
        return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Category> getCategoryList() {
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            CollectionReference categoryRef = dbFirestore.collection("category");

            ApiFuture<QuerySnapshot> future = categoryRef.get();
            QuerySnapshot querySnapshot = future.get();

            List<Category> categories = querySnapshot.toObjects(Category.class);
            return categories;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Category getCategory(int ID) {
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            DocumentReference documentReference = dbFirestore.collection("category").document(String.valueOf(ID));

            ApiFuture<DocumentSnapshot> future = documentReference.get();
            DocumentSnapshot documentSnapshot = future.get();

            return documentSnapshot.toObject(Category.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }

    @Override
    public boolean removeCategory(int ID) {
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            DocumentReference documentReference = dbFirestore.collection("category").document(String.valueOf(ID));

            ApiFuture<WriteResult> future = documentReference.delete();
            future.get();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isNotNullCategory(int ID) {
        if (getCategory(ID) == null) {
            return false;
        }

        return true;
    }

}
