package com.swp391.jewelrysalesystem.models;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class GoldPriceByRatio {
    private LocalDateTime date;
    private double goldPrice;
}
