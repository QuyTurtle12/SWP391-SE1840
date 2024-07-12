package com.swp391.jewelrysalesystem.models;

import java.util.HashMap;
import java.util.Map;

import com.swp391.jewelrysalesystem.services.StatusUpdatable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Counter implements StatusUpdatable{
    private int ID;
    private double sale;
    private Boolean status;

    @Override
    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public Boolean getStatus() {
        return status;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> result = new HashMap<>();
        result.put("id", ID);
        result.put("sale", sale);
        result.put("status", status);
        return result;
    }
}
