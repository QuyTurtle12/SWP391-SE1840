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
import com.swp391.jewelrysalesystem.models.Order;
import com.swp391.jewelrysalesystem.models.OrderDTO;

@Service
public class OrderService implements IOrderService{

    @Override
    public List<Order> getOrderList() {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference orderRef = dbFirestore.collection("order");

        try {
            ApiFuture<QuerySnapshot> future = orderRef.get();
            QuerySnapshot querySnapshot = future.get();

            List<Order> orderList = new ArrayList<>();
            for (QueryDocumentSnapshot document : querySnapshot) {
                orderList.add(document.toObject(Order.class));
            }
            return orderList;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Order getOrder(int ID) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference orderRef = dbFirestore.collection("order").document(String.valueOf(ID));

        try {
            ApiFuture<DocumentSnapshot> future = orderRef.get();
            DocumentSnapshot document = future.get();

            if (document.exists()) {
                Order order = document.toObject(Order.class);
                return order;
            } else {
                System.out.println("User document with orderID" + ID + "does not exist");
                return null;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Order> searchOrderList(String input, String filter, List<Order> orderList) {
        List<Order> searchedOrderList = new ArrayList<>();
        ICustomerService customerService = new CustomerService();
        switch (filter) {
            case "ByID":
                for (Order order : orderList) {
                    if (String.valueOf(order.getID()).equals(input)) {
                        searchedOrderList.add(order);
                    }
                }
                break;
            case "ByPhoneNumber":
                for (Order order : orderList) {
                    if (customerService.getPhoneNumber(order.getCustomerID()).equals(input)) {
                        searchedOrderList.add(order);
                    }
                }
                break;
            default:
                break;
        }
        
        return searchedOrderList;
    }

    @Override
    public Order saveOrder(Order order) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("order")
                .document(String.valueOf(order.getID()));

        try {
            ApiFuture<WriteResult> future = documentReference.set(order);
            future.get();
            return order;
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error saving order document: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public OrderDTO saveOrderDTO(OrderDTO orderDTO) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference orderRef = dbFirestore.collection("order").document(String.valueOf(orderDTO.getOrderID()))
        .collection("orderDTO").document(String.valueOf(orderDTO.getProductID()));

        try {
            ApiFuture<WriteResult> future = orderRef.set(orderDTO);
            future.get();
            return orderDTO;
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error saving orderDTO document: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean isNotNullOrder(int ID) {
        if (getOrder(ID) == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isNotNullStaff(int ID) {
        UserService userService = new UserService();
        return userService.isNotNullUser(ID);
    }

    @Override
    public boolean isNotNullCounter(int ID) {
        CounterService counterService = new CounterService();
        return counterService.isNotNullCounter(ID);
    }

    @Override
    public boolean isNotNullCustomer(int ID) {
        CustomerService customerService = new CustomerService();
        return customerService.isNotNullCustomer(ID);
    }
    
}
