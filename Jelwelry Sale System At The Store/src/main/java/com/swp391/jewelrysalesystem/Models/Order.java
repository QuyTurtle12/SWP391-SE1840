package com.swp391.jewelrysalesystem.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Order {
    private int ID;
    private LocalDateTime date;
    private int staffID;
    private int counterID;
    private int customerID;
    private double totalPrice;
    private double discountApplied;
}
