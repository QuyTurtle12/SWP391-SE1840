package com.swp391.jewelrysalesystem.Models;

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
    private double totalPrice;
    private int coupon; //Promotion in form of code
}
