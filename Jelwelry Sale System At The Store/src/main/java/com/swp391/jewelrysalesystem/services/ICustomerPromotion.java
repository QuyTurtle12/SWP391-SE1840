package com.swp391.jewelrysalesystem.services;

import java.util.List;

import com.swp391.jewelrysalesystem.models.CustomerPromotion;

public interface ICustomerPromotion {
    boolean saveCustomerPromotion(CustomerPromotion customerPromotion);

    List<CustomerPromotion> getCustomerPromotionList();

    List<CustomerPromotion> getCustomerAvailableCoupons(double totalPrice);

    CustomerPromotion getCustomerPromotion(int ID);

    CustomerPromotion getCustomerPromotion(String name);

    boolean changeStatus(int ID);

    int generateID();

    String isGeneralValidated(String discountName, double discountRate);
}
