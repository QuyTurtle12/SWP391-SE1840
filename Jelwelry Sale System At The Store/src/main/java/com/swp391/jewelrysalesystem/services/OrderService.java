package com.swp391.jewelrysalesystem.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.swp391.jewelrysalesystem.models.Order;
import com.swp391.jewelrysalesystem.models.OrderDTO;
import com.swp391.jewelrysalesystem.models.User;

@Service
public class OrderService implements IOrderService {

    private IGenericService<Order> genericService;
    private ICustomerService customerService;
    private IUserService userService;
    private ICounterService counterService;

    @Autowired
    public OrderService(IGenericService<Order> genericService, ICustomerService customerService,
            IUserService userService, ICounterService counterService) {
        this.genericService = genericService;
        this.customerService = customerService;
        this.userService = userService;
        this.counterService = counterService;
    }

    @Override
    public List<Order> getOrderList() {
        try {
            return genericService.getList("order", Order.class);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<OrderDTO> getAllOrderDetails() {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        List<OrderDTO> allOrderDetails = new ArrayList<>();

        try {
            // Fetch all orders
            List<Order> orders = getOrderList();
            if (orders == null) {
                return null;
            }

            // Iterate through each order and fetch its OrderDTOs
            for (Order order : orders) {
                CollectionReference collectionReference = dbFirestore.collection("order")
                        .document(String.valueOf(order.getID()))
                        .collection("orderDTO");

                ApiFuture<QuerySnapshot> future = collectionReference.get();
                QuerySnapshot querySnapshot = future.get();
                List<OrderDTO> orderDetails = querySnapshot.toObjects(OrderDTO.class);
                allOrderDetails.addAll(orderDetails);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }

        return allOrderDetails;
    }

    @Override
    public Order getOrder(int ID) {
        try {
            return genericService.getByField(ID, "id", "order", Order.class);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Order> searchOrderList(String input, String filter, List<Order> orderList) {
        List<Order> searchedOrderList = new ArrayList<>();
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
    public boolean saveOrder(Order order) {
        return genericService.saveObject(order, "order", order.getID());
    }

    @Override
    public boolean saveOrderDTO(OrderDTO orderDTO) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference orderRef = dbFirestore.collection("order").document(String.valueOf(orderDTO.getOrderID()))
                .collection("orderDTO").document(String.valueOf(orderDTO.getProductID()));

        try {
            ApiFuture<WriteResult> future = orderRef.set(orderDTO);
            future.get();
            return true;
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error saving orderDTO document: " + e.getMessage());
            e.printStackTrace();
            return false;
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
        try {
            User user = userService.getUserByField(ID, "id", "user");
            if (user != null && user.getRoleID() == 1) {
                return true;
            }

            return false;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isNotNullCounter(int ID) {
        return counterService.isNotNullCounter(ID);
    }

    @Override
    public List<OrderDTO> getOrderDetailList(int orderID) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference collectionReference = dbFirestore.collection("order").document(String.valueOf(orderID))
                .collection("orderDTO");

        try {
            ApiFuture<QuerySnapshot> future = collectionReference.get();
            QuerySnapshot querySnapshot = future.get();
            return querySnapshot.toObjects(OrderDTO.class);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String isGeneralValidated(int staffID, int counterID, String customerGender, String customerName,
            double discountApplied) {

        if (!isNotNullStaff(staffID)) {
            return "Staff ID " + staffID + " is not existing";
        }

        if (!isNotNullCounter(counterID)) {
            return "Counter ID " + counterID + " is not existing";
        }

        if (!customerGender.equals("Male") && !customerGender.equals("Female") && !customerGender.equals("Other")) {
            return "Incorrect gender format";
        }

        if (!Pattern.matches("^[a-zA-Z]+$", customerName)) {
            return "Incorrect name format! Must contain only alphabetic characters";
        }

        // For example 0.1 stand for 10%
        if (discountApplied < 0 || discountApplied > 1) {
            return "Incorrect discount input! input must be in range between 0 and 1";
        }

        return null;
    }

    @Override
    public int generateID() {
        return genericService.generateID("order", Order.class, Order::getID);
    }

    @Override
    public List<Order> getOrderListInSpecificTime(LocalDate startDate, LocalDate endDate) {
        List<Order> orders = getOrderList();

        List<Order> ordersInSpecificTime = new ArrayList<>();

        for (Order order : orders) {
            // Convert Timestamp to LocalDate
            LocalDate orderDate = order.getDate().toSqlTimestamp().toLocalDateTime().toLocalDate();

            // Check if orderDate is within the specified range
            if (!orderDate.isBefore(startDate) && !orderDate.isAfter(endDate)) {
                ordersInSpecificTime.add(order);
            }
        }

        return ordersInSpecificTime;
    }
}
