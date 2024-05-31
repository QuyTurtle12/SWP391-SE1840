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
import com.google.firebase.cloud.FirestoreClient;
import com.swp391.jewelrysalesystem.models.Customer;

@Service
public class CustomerService implements ICustomerService {

    @Override
    public String getPhoneNumber(int ID) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("customer").document(String.valueOf(ID));

        try {
            ApiFuture<DocumentSnapshot> future = documentReference.get();
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                Customer customer = document.toObject(Customer.class);
                return customer.getContactInfo();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Customer> getCustomerList() {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference customerRef = dbFirestore.collection("customer");

        try {
            ApiFuture<QuerySnapshot> future = customerRef.get();
            QuerySnapshot querySnapshot = future.get();

            List<Customer> customerList = new ArrayList<>();
            for (QueryDocumentSnapshot document : querySnapshot) {
                customerList.add(document.toObject(Customer.class));
            }
            return customerList;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Customer getCustomer(int ID) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference customerRef = dbFirestore.collection("customer").document(String.valueOf(ID));

        try {
            ApiFuture<DocumentSnapshot> future = customerRef.get();
            DocumentSnapshot document = future.get();

            if (document.exists()) {
                Customer customer = document.toObject(Customer.class);
                return customer;
            } else {
                System.out.println("User document with customerID" + ID + "does not exist");
                return null;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Customer> searchCustomerList(String input, String filter, List<Customer> customerList) {
        List<Customer> searchedCustomerList = new ArrayList<>();

        switch (filter) {
            case "ByPhoneNumber":
                for (Customer customer : customerList) {
                    if (customer.getContactInfo().equals(input)) {
                        searchedCustomerList.add(customer);
                    }
                }
                break;
        
            default:
                throw new IllegalArgumentException("Invalid filter: " + filter); 
        }
        return searchedCustomerList;
    }
}
