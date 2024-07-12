package com.swp391.jewelrysalesystem.models;

import com.swp391.jewelrysalesystem.services.StatusUpdatable;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements StatusUpdatable {
    private int ID;
    private int roleID;// 1 for STAFF, 2 for MANAGER, 3 for ADMIN
    private String fullName;
    private String password;
    private String email;
    private String gender;
    private String contactInfo;
    private int counterID;
    private Boolean status;

    private double sale; // staff only

    @Override
    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public Boolean getStatus() {
        return status;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("id", ID);
        result.put("fullName", fullName);
        result.put("email", email);
        result.put("contactInfo", contactInfo);
        result.put("roleID", roleID);
        result.put("status", status);
        result.put("counterID", counterID);
        return result;
    }

    public Map<String, Object> toMapWithSale() {
        Map<String, Object> result = toMap();
        // Add sale to the map only if the user is a staff member
        if (roleID == 1) {
            result.put("sale", sale);
        }

        return result;
    }

}
