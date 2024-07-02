package com.swp391.jewelrysalesystem.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swp391.jewelrysalesystem.models.Promotion;

@Service
public class PromotionService implements IPromotionService {
    private final int NO_PROMOTION = 0;

    private IGenericService<Promotion> genericService;

    @Autowired
    public PromotionService(IGenericService<Promotion> genericService){
        this.genericService = genericService;
    }

    @Override
    public boolean savePromotion(Promotion promotion) {
        return genericService.saveObject(promotion, "promotion", promotion.getID());
    }

    @Override
    public List<Promotion> getPromotionList() {
        try {
            return genericService.getList("promotion", Promotion.class);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean changePromotionStatus(int ID) {
        return genericService.changeStatus(ID, "promotion", Promotion.class);
    }

    @Override
    public List<Promotion> searchPromotions(String input, String filter, List<Promotion> promotions) {
        List<Promotion> searchedPromotionList = new ArrayList<>();
        switch (filter) {
            case "ByID":
                for (Promotion promotion : promotions) {
                    if (String.valueOf(promotion.getID()).toLowerCase().equals(input)) {
                        searchedPromotionList.add(promotion);
                    }
                }
                break;
            case "ByStatus":
                for (Promotion promotion : promotions) {
                    if (promotion.getStatus().toString().toLowerCase().equals(input)) {
                        searchedPromotionList.add(promotion);
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid filter: " + filter);
        }

        return searchedPromotionList;
    }

    @Override
    public Promotion getPromotion(int ID) {
        try {
            return genericService.getByField(ID, "id", "null", Promotion.class);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean isNotNullPromotion(int ID) {
        
        if (ID == NO_PROMOTION) {
            return true;
        }

        if (getPromotion(ID) == null) {
            return false;
        }
        return true;
    }

    @Override
    public int generateID() {
        return genericService.generateID("promotion", Promotion.class, Promotion::getID);
    }

}
