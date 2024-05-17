package com.swp391.jewelrysalesystem.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Product {
    private int ID;
    private String name;
    private double price;
    private double refundPrice;
    private String description;
    private String barcode;
    private double goldWeight;
    private double laborCost;
    private double stoneCost;
    private int stock;
    private int promotionID;
    private String category;
    private boolean status;
}
