package com.swp391.jewelrysalesystem.services;

import java.util.List;

import com.swp391.jewelrysalesystem.models.Promotion;

public interface IPromotionService {
    boolean savePromotion(Promotion promotion);

    List<Promotion> getPromotionList();

    List<Promotion> searchPromotions(String input, String filter, List<Promotion> promotions);

    boolean changePromotionStatus(int ID);

    Promotion getPromotion(int ID);

    boolean isNotNullPromotion(int ID);
}
