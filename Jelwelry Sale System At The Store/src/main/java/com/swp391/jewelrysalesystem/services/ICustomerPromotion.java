package com.swp391.jewelrysalesystem.services;

import java.util.List;

import com.swp391.jewelrysalesystem.models.CustomerPromotion;

public interface ICustomerPromotion {
    boolean saveCustomerPromotion(CustomerPromotion customerPromotion);

    List<CustomerPromotion> getCustomerPromotionList();

    CustomerPromotion getCustomerPromotion(int ID);

    boolean changeStatus(int ID);
}
