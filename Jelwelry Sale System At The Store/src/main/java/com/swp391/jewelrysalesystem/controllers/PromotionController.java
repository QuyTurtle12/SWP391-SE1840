package com.swp391.jewelrysalesystem.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.swp391.jewelrysalesystem.models.Promotion;
import com.swp391.jewelrysalesystem.services.IPromotionService;

import java.util.List;

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

    @Autowired
    public PromotionController(IPromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @PostMapping("/promotion/add")
    public Promotion addPromotion(
            @RequestParam int id,
            @RequestParam String description,
            @RequestParam double discountRate) {

        Promotion newPromotion = new Promotion();
        newPromotion.setID(id);
        newPromotion.setDescription(description);
        newPromotion.setDiscountRate(discountRate);
        newPromotion.setStatus(true);

        return promotionService.savePromotion(newPromotion);
    }

    @PutMapping("/promotion/{id}/update-info")
    public Promotion updatePromotion(
            @PathVariable int id,
            @RequestParam String description,
            @RequestParam double discountRate) {

        Promotion existingPromotion = promotionService.getPromotion(id);
        if (promotionService != null) {
            existingPromotion.setDescription(description);
            existingPromotion.setDiscountRate(discountRate);
            return promotionService.savePromotion(existingPromotion);
        } else {
            throw new RuntimeException("Promotion with ID " + id + " not found.");
        }
    }

    @PutMapping("/promotion/change-status")
    public Promotion changePromotionStatus(@RequestParam int id) {
        return promotionService.changePromotionStatus(id);
    }

    @GetMapping("/promotion/list")
    public ResponseEntity<List<Promotion>> getPromotionList() {
        try {
            List<Promotion> promotionList = promotionService.getPromotionList();
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

    // Search by id, status
    @GetMapping("/promotion/list/search")
    public ResponseEntity<List<Promotion>> searchPromotion(
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
