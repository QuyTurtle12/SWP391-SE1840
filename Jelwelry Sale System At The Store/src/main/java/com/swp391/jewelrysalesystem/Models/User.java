package com.swp391.jewelrysalesystem.models;

import com.swp391.jewelrysalesystem.services.StatusUpdatable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class User implements StatusUpdatable{
    private int ID;
    private int roleID;
    private String fullName;
    private String password;
    private String email;
    private String gender;
    private String contactInfo;
    private int counterID;
    private Boolean status;
    
    @Override
    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public Boolean getStatus(){
        return status;
    }

}
