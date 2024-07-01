package com.swp391.jewelrysalesystem.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.swp391.jewelrysalesystem.models.Customer;

@Service
public class CustomerService implements ICustomerService {

    private IGenericService<Customer> genericService;

    @Autowired
    public CustomerService(IGenericService<Customer> genericService) {
        this.genericService = genericService;
    }

    @Override
    public String getPhoneNumber(int ID) {
        try {
            Customer customer = genericService.getByField(ID, "id", "customer", Customer.class);
            return customer.getContactInfo();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Customer> getCustomerList() {
        try {
            return genericService.getList("customer", Customer.class);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
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

    @Override
    public boolean isNotNullCustomer(int ID) throws InterruptedException, ExecutionException {
        return genericService.getByField(ID, "id", "customer", Customer.class) != null ? true : false;
    }

    @Override
    public Customer getCustomer(int ID) {
        try {
            return genericService.getByField(ID, "id", "customer", Customer.class);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean isNotExistedPhoneNum(String contactInfo) {
        try {
            Customer existedPhoneNum = genericService.getByField(contactInfo, "contactInfo", "customer",
                    Customer.class);

            if (existedPhoneNum == null) {
                return true;
            }

            return false;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Customer getCustomerByPhone(String contactInfo) {
        try {
            return genericService.getByField(contactInfo, "contactInfo", "customer", Customer.class);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean saveCustomer(Customer customer) {
        int newID = generateCustomerID();
        if (newID == 0) {
            return false; // error
        }
        customer.setID(newID);
        return genericService.saveObject(customer, "customer", customer.getID());
    }

    public int generateCustomerID() {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference collectionReference = dbFirestore.collection("customer");

        try {
            ApiFuture<QuerySnapshot> future = collectionReference.get();
            QuerySnapshot querySnapshot = future.get();

            List<Customer> customers = querySnapshot.toObjects(Customer.class);

            int maxID = 0; // default ID value is 1 but to return 1 we have to start with 0 because we add
                           // 1 when we return
            if (!customers.isEmpty()) {
                maxID = customers.stream()
                        .max(Comparator.comparingInt(Customer::getID))
                        .get()
                        .getID();
            }

            return maxID + 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
