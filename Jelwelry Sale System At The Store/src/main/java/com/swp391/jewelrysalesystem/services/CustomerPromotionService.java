package com.swp391.jewelrysalesystem.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.swp391.jewelrysalesystem.models.CustomerPromotion;

@Service
public class CustomerPromotionService implements ICustomerPromotion{

    @Override
    public boolean saveCustomerPromotion(CustomerPromotion customerPromotion) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveCustomerPromotion'");
    }

    @Override
    public List<CustomerPromotion> getCustomerPromotionList() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCustomerPromotionList'");
    }

    @Override
    public CustomerPromotion getCustomerPromotion(int ID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCustomerPromotion'");
    }

    @Override
    public boolean changeStatus(int ID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changeStatus'");
    }

}
