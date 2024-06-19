package com.swp391.jewelrysalesystem.services;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.swp391.jewelrysalesystem.models.Customer;
import com.swp391.jewelrysalesystem.models.User;

public interface ICustomerService {
    String getPhoneNumber(int ID);

    List<Customer> getCustomerList();

    Customer getCustomer(int ID);

    List<Customer> searchCustomerList(String input, String filter, List<Customer> customerList);

    boolean isNotNullCustomer(int ID) throws InterruptedException, ExecutionException;

    boolean isNotExistedPhoneNum(String contactInfo);
}
