package com.swp391.jewelrysalesystem.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class User {
    private int ID;
    private int roleID;
    private String fullName;
    private String password;
    private String email;
    private String gender;
    private String contactInfo;
    private int counterID;
    private Boolean status;
}
