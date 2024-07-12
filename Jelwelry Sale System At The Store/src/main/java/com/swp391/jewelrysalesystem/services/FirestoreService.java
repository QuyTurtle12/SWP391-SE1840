package com.swp391.jewelrysalesystem.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteBatch;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.swp391.jewelrysalesystem.models.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class FirestoreService {

    public void bulkAddData(String collectionName, List<Product> dataList)
            throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        WriteBatch batch = db.batch();

        for (Product product : dataList) {
            batch.set(db.collection(collectionName).document(String.valueOf(product.getID())), product);
        }

        ApiFuture<List<WriteResult>> future = batch.commit();
        future.get();
    }
}
