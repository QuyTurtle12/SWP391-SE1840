package com.swp391.jewelrysalesystem.models;

import com.swp391.jewelrysalesystem.services.StatusUpdatable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Product implements StatusUpdatable {
    private int ID;
    private String img;
    private String name;
    private double price;
    private double refundPrice;
    private String description;
    private double goldWeight;
    private double laborCost;
    private double stoneCost;
    private int stock;
    private int promotionID;
    private int categoryID;
    private Boolean status;

    @Override
    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public Boolean getStatus(){
        return status;
    }
}
