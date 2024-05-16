package com.swp391.jewelrysalesystem.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class User {
    private int ID;
    private String fullName;
    private String password;
    private String role;
    private String gender;
    private String contactInfo;
    private String counterID;
    private String status;

}
