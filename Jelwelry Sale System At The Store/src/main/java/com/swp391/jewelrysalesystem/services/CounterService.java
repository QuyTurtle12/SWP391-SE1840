package com.swp391.jewelrysalesystem.services;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.swp391.jewelrysalesystem.models.Counter;
import com.swp391.jewelrysalesystem.models.Order;

@Service
public class CounterService implements ICounterService {

    private IGenericService<Counter> genericService;

    @Autowired
    public CounterService(IGenericService<Counter> genericService) {
        this.genericService = genericService;
    }

    @Override
    public boolean saveCounter(Counter counter) {
        return genericService.saveObject(counter, "counter", counter.getID());
    }

    @Override
    public boolean removeCounter(int ID) {
        return genericService.deleteObject(ID, "counter");
    }

    @Override
    public List<Counter> getCountersList() {
        try {
            return genericService.getList("counter", Counter.class);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Counter getCounter(int ID) {
        try {
            return genericService.getByField(ID, "id", "counter", Counter.class);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean changeStatus(int ID) {
        return genericService.changeStatus(ID, "counter", Counter.class);
    }

    @Override
    public boolean isNotNullCounter(int ID) {
        return getCounter(ID) != null ? true : false;
    }

    @Override
    public int generateID() {
        return genericService.generateID("counter", Counter.class, Counter::getID);
    }


    @Override
    public double calculateCounterSale(int counterID) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference ordersCollection = dbFirestore.collection("order");

        try {
            ApiFuture<QuerySnapshot> future = ordersCollection.whereEqualTo("counterID", counterID).get();
            QuerySnapshot orders = future.get();

            double totalSale = 0.0;
            for (QueryDocumentSnapshot document : orders) {
                Order order = document.toObject(Order.class);
                totalSale += order.getTotalPrice();
            }
            return totalSale;
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error calculating counter sale: " + e.getMessage());
            e.printStackTrace();
            return 0.0;
        }
    }

}
