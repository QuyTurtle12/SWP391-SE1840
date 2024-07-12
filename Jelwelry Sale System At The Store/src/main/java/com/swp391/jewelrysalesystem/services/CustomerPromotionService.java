package com.swp391.jewelrysalesystem.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.swp391.jewelrysalesystem.models.CustomerPromotion;

@Service
public class CustomerPromotionService implements ICustomerPromotion {

    private IGenericService<CustomerPromotion> genericService;

    @Autowired
    public CustomerPromotionService(IGenericService<CustomerPromotion> genericService) {
        this.genericService = genericService;
    }

    @Override
    public boolean saveCustomerPromotion(CustomerPromotion customerPromotion) {
        return genericService.saveObject(customerPromotion, "customer-promotion", customerPromotion.getID());
    }

    @Override
    public List<CustomerPromotion> getCustomerPromotionList() {
        try {
            return genericService.getList("customer-promotion", CustomerPromotion.class);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public CustomerPromotion getCustomerPromotion(int ID) {
        try {
            return genericService.getByField(ID, "id", "customer-promotion", CustomerPromotion.class);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean changeStatus(int ID) {
        return genericService.changeStatus(ID, "customer-promotion", CustomerPromotion.class);
    }

    @Override
    public int generateID() {
        return genericService.generateID("customer-promotion", CustomerPromotion.class, CustomerPromotion::getID);
    }

    @Override
    public String isGeneralValidated(String discountName, double discountRate) {
        if (discountName.isBlank()) {
            return "Invalid discount name";
        }

        if (discountRate <= 0 || discountRate > 1) {
            return "Invalid discount rate";
        }

        return null;
    }

    @Override
    public CustomerPromotion getCustomerPromotion(String name) {
        try {
            return genericService.getByField(name, "discountName", "customer-promotion", CustomerPromotion.class);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<CustomerPromotion> getCustomerAvailableCoupons(double totalPrice) {
        List<CustomerPromotion> promotionList = getCustomerPromotionList();

        List<CustomerPromotion> coupons = new ArrayList<>();

        for (CustomerPromotion promotion : promotionList) {
            if (promotion.getDiscountType() == "Normal") {
                coupons.add(promotion);
                continue;
            }

            if (promotion.getDiscountType() == "Accepted Price") {
                if (totalPrice >= Double.parseDouble(promotion.getDiscountCondition())) {
                    coupons.add(promotion);
                    continue;
                }
            }
        }

        return coupons;
        
    }

}
