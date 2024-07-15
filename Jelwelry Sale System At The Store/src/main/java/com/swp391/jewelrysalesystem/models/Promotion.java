package com.swp391.jewelrysalesystem.models;

import java.util.HashMap;
import java.util.Map;

import com.swp391.jewelrysalesystem.services.StatusUpdatable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Promotion implements StatusUpdatable {
    private int ID;
    private String description;
    private double discountRate;
    private Boolean status;
    
    
    @Override
    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public Boolean getStatus(){
        return status;
    }

        public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("id", ID);
        result.put("description", description);
        result.put("discountRate", discountRate);
        result.put("status", status);

        return result;
    }
}
