package com.swp391.jewelrysalesystem.models;

import com.swp391.jewelrysalesystem.services.StatusUpdatable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class CustomerPromotion implements StatusUpdatable {
    String discountName;
    double discountRate; //Discount rate is in range bewteen 0 to 1
    Boolean status;
    @Override

    public void setStatus(boolean status) {
        this.status = status;
    }
}
