package com.swp391.jewelrysalesystem.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
