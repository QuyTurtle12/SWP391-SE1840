package com.swp391.jewelrysalesystem.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.swp391.jewelrysalesystem.models.Promotion;
import com.swp391.jewelrysalesystem.services.IProductService;
import com.swp391.jewelrysalesystem.services.IPromotionService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api")

public class PromotionController {
    private IPromotionService promotionService;
    private IProductService productService;

    @Autowired
    public PromotionController(IPromotionService promotionService, IProductService productService) {
        this.promotionService = promotionService;
        this.productService = productService;
    }

    @PostMapping("v2/promotions")
    public ResponseEntity<String> addPromotionV2(
            @RequestParam String description,
            @RequestParam double discountRate) {

        int id = promotionService.generateID();

        Promotion newPromotion = new Promotion();
        newPromotion.setID(id);
        newPromotion.setDescription(description);
        newPromotion.setDiscountRate(discountRate);
        newPromotion.setStatus(true);

        if (promotionService.savePromotion(newPromotion)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Error saving promotion!");
        }

    }

    @PutMapping("v2/promotions/{id}")
    public ResponseEntity<String> updatePromotionV2(
            @PathVariable int id,
            @RequestParam String description,
            @RequestParam double discountRate) {

        if (!promotionService.isNotNullPromotion(id)) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body("Promotion ID " + id + " is not existing");
        }

        Promotion existingPromotion = promotionService.getPromotion(id);
        existingPromotion.setDescription(description);
        existingPromotion.setDiscountRate(discountRate);

        if (promotionService.savePromotion(existingPromotion)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Error saving promotion!");
        }
    }

    @PutMapping("v2/promotions/status")
    public ResponseEntity<String> changePromotionStatusV2(@RequestParam int id) {
        if (!promotionService.isNotNullPromotion(id)) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body("Promotion ID " + id + " is not existing");
        }

        if (promotionService.changePromotionStatus(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Error updating promotion!");
    }

    @GetMapping("v2/promotions/promotion")
    public ResponseEntity<Promotion> getPromotionV2(@RequestParam int promotionID) {
        try {
            Promotion promotion = promotionService.getPromotion(promotionID);

            if (promotion == null) {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
            }

            return ResponseEntity.ok(promotion);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("v2/promotions")
    public ResponseEntity<List<Map<String, Object>>> getPromotionListV2() {
        try {
            List<Promotion> promotionList = promotionService.getPromotionList();

            if (promotionList == null || promotionList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
            }

            List<Map<String, Object>> response = new ArrayList<>();
            for (Promotion promotion : promotionList) {
                Map<String, Object> map = promotion.toMap();
                response.add(map);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("v2/promotions/available-promotion")
    public ResponseEntity<List<Map<String, Object>>> getAvailablePromotionListV2() {
        try {
            List<Promotion> promotionList = promotionService.getPromotionList();

            if (promotionList == null || promotionList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
            }

            List<Map<String, Object>> response = new ArrayList<>();
            for (Promotion promotion : promotionList) {
                if (promotion.getStatus() == true) {
                    Map<String, Object> map = promotion.toMap();
                    response.add(map);
                }
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Search by id, status
    @GetMapping("v2/promotions/search")
    public ResponseEntity<List<Promotion>> searchPromotionV2(
            @RequestParam String input,
            @RequestParam String filter) {

        try {
            List<Promotion> promotionList = promotionService.getPromotionList();
            promotionList = promotionService.searchPromotions(input, filter, promotionList);

            if (promotionList != null && !promotionList.isEmpty()) {
                return ResponseEntity.ok(promotionList);
            } else {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
