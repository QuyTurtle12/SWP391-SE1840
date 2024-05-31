package com.swp391.jewelrysalesystem.controllers;

import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swp391.jewelrysalesystem.models.Customer;
import com.swp391.jewelrysalesystem.services.ICustomerService;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    private ICustomerService customerService;

    @Autowired
    public CustomerController(ICustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Customer>> getCustomerList() {
        try {
            List<Customer> CustomerList = customerService.getCustomerList();

            if (CustomerList != null && !CustomerList.isEmpty()) {
                return ResponseEntity.ok(CustomerList);
            } else {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<Customer> getCustomerList(@RequestParam int id) {
        try {
            Customer Customer = customerService.getCustomer(id);

            if (Customer != null) {
                return ResponseEntity.ok(Customer);
            } else {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/list/search")
    public ResponseEntity<List<Customer>> searchCustomerList(
            @RequestParam String input,
            @RequestParam String filter) {
        try {
            List<Customer> CustomerList = customerService.searchCustomerList(input, filter,
                    customerService.getCustomerList());

            if (CustomerList != null && !CustomerList.isEmpty()) {
                return ResponseEntity.ok(CustomerList);
            } else {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
