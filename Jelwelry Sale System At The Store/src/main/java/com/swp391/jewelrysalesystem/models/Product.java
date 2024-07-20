package com.swp391.jewelrysalesystem.models;

import java.util.HashMap;
import java.util.Map;

import com.swp391.jewelrysalesystem.services.StatusUpdatable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product implements StatusUpdatable {
    private int ID;
    private String img;
    private String name;
    private double refundRate;
    private double desiredProditMargin;
    private double price;
    private double refundPrice;
    private double discountPrice;
    private String description;
    private double goldWeight;
    private double laborCost;
    private double stoneCost;
    private String stoneName;
    private String stoneType;
    private int stock;
    private int promotionID;
    private int categoryID;
    private Boolean status;

    @Override
    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public Boolean getStatus() {
        return status;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> productMap = new HashMap<>();
        productMap.put("id", ID);
        productMap.put("img", img);
        productMap.put("name", name);
        productMap.put("refundRate", refundRate);
        productMap.put("desiredProditMargin", desiredProditMargin);
        productMap.put("price", price);
        productMap.put("refundPrice", refundPrice);
        productMap.put("discountPrice",discountPrice);
        productMap.put("description", description);
        productMap.put("goldWeight", goldWeight);
        productMap.put("laborCost", laborCost);
        productMap.put("stoneCost", stoneCost);
        productMap.put("stoneName", stoneName);
        productMap.put("stoneType", stoneType);
        productMap.put("stock", stock);
        productMap.put("promotionID", promotionID);
        productMap.put("categoryID", categoryID);
        productMap.put("status", status);

        return productMap;
    }
}
