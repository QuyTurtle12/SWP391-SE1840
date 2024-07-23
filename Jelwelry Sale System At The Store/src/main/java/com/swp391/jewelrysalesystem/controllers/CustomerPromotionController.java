package com.swp391.jewelrysalesystem.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swp391.jewelrysalesystem.models.CustomerPromotion;
import com.swp391.jewelrysalesystem.services.ICustomerPromotion;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api")
public class CustomerPromotionController {
    private ICustomerPromotion customerPromotionService;

    @Autowired
    public CustomerPromotionController(ICustomerPromotion customerPromotionService) {
        this.customerPromotionService = customerPromotionService;
    }

    // Applied for checkout
    @GetMapping("/customer-promotions/customer-coupons")
    public ResponseEntity<List<CustomerPromotion>> getCustomerAvailableCoupons(@RequestParam double totalPrice) {
        List<CustomerPromotion> couponList = customerPromotionService.getCustomerAvailableCoupons(totalPrice);

        return ResponseEntity.ok().body(couponList);
    }

    @PostMapping("/customer-promotions")
    public ResponseEntity<String> addCustomerPromotion(@RequestParam String discountName,
            @RequestParam String discountDescription,
            @RequestParam String discountType, // Normal, Accepted Price
            @RequestParam String discountCondition,
            @RequestParam double discountRate) {

        String error = customerPromotionService.isGeneralValidated(discountName, discountRate);

        if (error != null) {
            ResponseEntity.badRequest().body(error);
        }

        CustomerPromotion promotion = new CustomerPromotion();
        int promotionID = customerPromotionService.generateID();

        promotion.setID(promotionID);
        promotion.setDiscountName(discountName);
        promotion.setDiscountDescription(discountDescription);
        promotion.setDiscountType(discountType);
        promotion.setDiscountCondition(discountCondition);
        promotion.setDiscountRate(discountRate);
        promotion.setStatus(true);

        if (customerPromotionService.saveCustomerPromotion(promotion)) {
            return ResponseEntity.ok().body("Saved customer promotion successfully");
        } else {
            return ResponseEntity.internalServerError().body("Error saving customer promotion");
        }
    }

    @PutMapping("/customer-promotions/{ID}")
    public ResponseEntity<String> updateCustomerPromotion(@PathVariable int ID, @RequestParam String discountName,
            @RequestParam String discountType,
            @RequestParam String discountCondition,
            @RequestParam String discountDescription,
            @RequestParam double discountRate) {

        CustomerPromotion promotion = customerPromotionService.getCustomerPromotion(ID);

        if (promotion == null) {
            return ResponseEntity.badRequest().body("The promotion " + ID + " does not exist!");
        }

        String error = customerPromotionService.isGeneralValidated(discountName, discountRate);

        if (error != null) {
            ResponseEntity.badRequest().body(error);
        }

        promotion.setDiscountName(discountName);
        promotion.setDiscountDescription(discountDescription);
        promotion.setDiscountType(discountType);
        promotion.setDiscountCondition(discountCondition);
        promotion.setDiscountRate(discountRate);
        promotion.setStatus(true);

        if (customerPromotionService.saveCustomerPromotion(promotion)) {
            return ResponseEntity.ok().body("Saved customer promotion successfully");
        } else {
            return ResponseEntity.internalServerError().body("Error saving customer promotion");
        }
    }

    @PutMapping("/customer-promotions/{ID}/status")
    public ResponseEntity<String> changePromotionStatus(@PathVariable int ID) {
        CustomerPromotion promotion = customerPromotionService.getCustomerPromotion(ID);

        if (promotion == null) {
            return ResponseEntity.badRequest().body("The promotion " + ID + " does not exist!");
        }

        if (customerPromotionService.changeStatus(ID)) {
            return ResponseEntity.ok().body("Changed status successfully!");
        }

        return ResponseEntity.internalServerError().body("Error changing customer promotion status");
    }

    @GetMapping("/customer-promotions")
    public ResponseEntity<List<CustomerPromotion>> getCustomerPromotionList() {
        return ResponseEntity.ok(customerPromotionService.getCustomerPromotionList());
    }

    @GetMapping("/customer-promotions/promotion")
    public ResponseEntity<CustomerPromotion> getCustomerPromotion(@RequestParam int ID) {
        CustomerPromotion promotion = customerPromotionService.getCustomerPromotion(ID);

        if (promotion == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(promotion);
    }

    @GetMapping("/customer-promotions/promotion/name")
    public ResponseEntity<CustomerPromotion> getCustomerPromotion(@RequestParam String name) {
        CustomerPromotion promotion = customerPromotionService.getCustomerPromotion(name);

        if (promotion == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(promotion);
    }

}
