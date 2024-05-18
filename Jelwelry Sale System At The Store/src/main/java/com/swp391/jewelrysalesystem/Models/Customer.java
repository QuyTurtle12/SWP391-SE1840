package com.swp391.jewelrysalesystem.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Customer {
    private int ID;
    private String name;
    private String contactInfo;
    private String gender;
    private double point;
}
