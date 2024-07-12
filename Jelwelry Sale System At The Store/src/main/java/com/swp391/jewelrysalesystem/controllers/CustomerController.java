package com.swp391.jewelrysalesystem.controllers;

//
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swp391.jewelrysalesystem.models.Customer;
import com.swp391.jewelrysalesystem.models.Order;
import com.swp391.jewelrysalesystem.services.ICustomerService;
import com.swp391.jewelrysalesystem.services.IOrderService;

@RestController
@RequestMapping("/api")
public class CustomerController {
    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    public CustomerController(ICustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/v2/customers")
    public ResponseEntity<List<Customer>> getCustomerListV2() {
        try {
            List<Customer> CustomerList = customerService.getCustomerList();

            if (CustomerList == null || CustomerList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
            }

            return ResponseEntity.ok(CustomerList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/v2/customers/customer")
    public ResponseEntity<Customer> getCustomerV2(@RequestParam int id) {
        try {
            Customer Customer = customerService.getCustomer(id);

            if (Customer == null) {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
            }

            return ResponseEntity.ok(Customer);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/v2/customers/search")
    public ResponseEntity<List<Customer>> searchCustomerListV2(
            @RequestParam String input,
            @RequestParam String filter) {
        try {
            List<Customer> CustomerList = new ArrayList<>();
            CustomerList = customerService.searchCustomerList(input, filter,
                    customerService.getCustomerList());

            if (CustomerList == null || CustomerList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(CustomerList);
            }

            return ResponseEntity.ok(CustomerList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/v2/customers/top")
    public ResponseEntity<List<Map<String, Object>>> getTopCustomersByPurchases() {
        try {
            List<Customer> customers = customerService.getCustomerList();
            Map<Integer, Double> customerPurchases = new HashMap<>();

            List<Order> orders = orderService.getOrderList();
            for (Order order : orders) {
                int customerId = order.getCustomerID();
                double totalPrice = order.getTotalPrice();
                customerPurchases.put(customerId, customerPurchases.getOrDefault(customerId, 0.0) + totalPrice);
            }

            List<Map.Entry<Integer, Double>> sortedCustomers = customerPurchases.entrySet().stream()
                    .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                    .collect(Collectors.toList());

            List<Map.Entry<Integer, Double>> topCustomers = sortedCustomers.stream()
                    .limit(10)
                    .collect(Collectors.toList());

            List<Map<String, Object>> topCustomersList = new ArrayList<>();
            for (Map.Entry<Integer, Double> entry : topCustomers) {
                int customerId = entry.getKey();
                double totalPurchases = entry.getValue();
                Customer customer = customers.stream()
                        .filter(c -> c.getID() == customerId)
                        .findFirst()
                        .orElse(null);

                if (customer != null) {
                    Map<String, Object> customerMap = new HashMap<>();
                    customerMap.put("name", customer.getName());
                    customerMap.put("totalPurchases", totalPurchases);
                    topCustomersList.add(customerMap);
                }
            }

            return ResponseEntity.ok(topCustomersList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/v2/customers/customer/point")
    public ResponseEntity<String> getCustomerPoint(@RequestParam String customerPhone) {
        Customer customer = customerService.getCustomerByPhone(customerPhone);
        if (customer == null) {
            return ResponseEntity.badRequest().body("The phone number " + customerPhone + " is not existing");
        }
 
        return ResponseEntity.ok().body(String.valueOf(customer.getPoint()));
    }
    
}
