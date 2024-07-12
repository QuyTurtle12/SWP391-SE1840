package com.swp391.jewelrysalesystem.controllers;

//
import java.util.ArrayList;
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
@RequestMapping("/api")
public class CustomerController {
    private ICustomerService customerService;

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

    @GetMapping("/v2/customers/customer/point")
    public ResponseEntity<String> getCustomerPoint(@RequestParam String customerPhone) {
        Customer customer = customerService.getCustomerByPhone(customerPhone);
        if (customer == null) {
            return ResponseEntity.badRequest().body("The phone number " + customerPhone + " is not existing");
        }
 
        return ResponseEntity.ok().body(String.valueOf(customer.getPoint()));
    }
    
}
