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
import com.swp391.jewelrysalesystem.models.Promotion;

@Service
public class PromotionService implements IPromotionService {

    @Override
    public Promotion savePromotion(Promotion promotion) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("promotion").document(String.valueOf(promotion.getID()));

        ApiFuture<WriteResult> future = documentReference.set(promotion);
        try {
            future.get();
            return promotion;
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error saving promotion document: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Promotion> getPromotionList() {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference promotionCollection = dbFirestore.collection("promotion");

        try {
            ApiFuture<QuerySnapshot> future = promotionCollection.get();
            QuerySnapshot promotions = future.get();

            List<Promotion> promotionList = new ArrayList<>();
            for (QueryDocumentSnapshot document : promotions) {
                promotionList.add(document.toObject(Promotion.class));
            } 
            return promotionList;
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error retrieving promotion list: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Promotion changePromotionStatus(int ID) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference promotionRef = dbFirestore.collection("promotion").document(String.valueOf(ID));

        try {
            ApiFuture<DocumentSnapshot> future = promotionRef.get();
            DocumentSnapshot documentSnapshot = future.get();

            if (documentSnapshot.exists()) {
                Promotion promotion = documentSnapshot.toObject(Promotion.class);
                if (promotion != null) {
                    promotion.setStatus(!promotion.getStatus());
                    promotionRef.set(promotion);
                    return promotion;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error changing promotion status: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Promotion> searchPromotions(String input, String filter, List<Promotion> promotions) {
        List<Promotion> searchedPromotionList = new ArrayList<>();
        switch (filter) {
            case "ByID":
                for (Promotion promotion : promotions) {
                    if (String.valueOf(promotion.getID()).toLowerCase().equals(input)) {
                        searchedPromotionList.add(promotion);
                    }
                }
                break;
            case "ByStatus":
                for (Promotion promotion : promotions) {
                    if (promotion.getStatus().toString().toLowerCase().equals(input)) {
                        searchedPromotionList.add(promotion);
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid filter: " + filter);
        }

        return searchedPromotionList;
    }

    @Override
    public Promotion getPromotion(int ID) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference promotionRef = dbFirestore.collection("promotion").document(String.valueOf(ID));

        try {
            ApiFuture<DocumentSnapshot> future = promotionRef.get();
            DocumentSnapshot document = future.get();

            if (document.exists()) {
                Promotion promotion = document.toObject(Promotion.class);
                return promotion;
            }
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error retrieving promotion:" + ID);
            e.printStackTrace();
        }
        return null;
    }

}
