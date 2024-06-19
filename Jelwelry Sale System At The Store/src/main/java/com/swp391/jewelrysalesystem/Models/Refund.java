package com.swp391.jewelrysalesystem.models;

import com.google.cloud.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Refund {
    private int ID;
    private Timestamp date;
    private double totalPrice;
    private int customerID;
}
