package com.swp391.jewelrysalesystem.services;

import java.util.List;

import com.swp391.jewelrysalesystem.models.Customer;

public interface ICustomerService {
    String getPhoneNumber(int ID);

    List<Customer> getCustomerList();

    Customer getCustomer(int ID);

    List<Customer> searchCustomerList(String input, String filter, List<Customer> customerList);
}
