package com.swp391.jewelrysalesystem.models;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Refund {
    private int ID;
    private LocalDateTime date;
    private double totalAmount;
    private int customerID;
}
