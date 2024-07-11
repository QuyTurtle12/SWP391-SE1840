package com.swp391.jewelrysalesystem.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.google.cloud.Timestamp;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Order {
    private int ID;
    private Timestamp date;
    private int staffID;
    private int counterID;
    private int customerID;
    private double totalPrice;
    private int discountID;
    private double discountApplied;
}
