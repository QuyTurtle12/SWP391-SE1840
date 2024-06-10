package com.swp391.jewelrysalesystem.controllers;

import java.util.concurrent.ExecutionException;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import com.swp391.jewelrysalesystem.models.User;
import com.swp391.jewelrysalesystem.services.IUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/account")
public class UserController {

    private IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/{role}/register")
    public User addUser(
            @RequestParam int ID,
            @RequestParam String fullName,
            @RequestParam String gender,
            @RequestParam String email,
            @PathVariable String role,
            @RequestParam String password,
            @RequestParam String contactInfo,
            @RequestParam int counterID) {
        
        int roleID = 0;
        switch (role) {
            case "MANAGER":
                roleID = 2;
                break;
            case "STAFF":
                roleID = 1;
                break;
            default:
            throw new IllegalArgumentException("Invalid role: " + role);
        }
        User newUser = new User();
        newUser.setID(ID);
        newUser.setFullName(fullName);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setGender(gender);
        newUser.setContactInfo(contactInfo);
        newUser.setCounterID(counterID);
        newUser.setRoleID(roleID);
        newUser.setStatus(true);

        return userService.saveUser(newUser);
    }

    @PutMapping("/{role}/update-info")
    public User updateUserInfo(
            @RequestParam int ID,
            @RequestParam String fullName,
            @RequestParam String gender,
            @RequestParam String contactInfo,
            @RequestParam int counterID)
            throws InterruptedException, ExecutionException {

        User existingUser = userService.getUserByUserID(ID);

        if (existingUser != null) {
            existingUser.setFullName(fullName);
            existingUser.setGender(gender);
            existingUser.setContactInfo(contactInfo);
            existingUser.setCounterID(counterID);
            
            return userService.saveUser(existingUser);
        } else {
            throw new RuntimeException("User with ID " + ID + " not found.");
        }
    }

    @PutMapping("/{role}/change-status")
    public User changeUserStatus(@RequestParam int ID) {
        return userService.changeUserStatus(ID);
    }

    @GetMapping("/dashboard")
    public RedirectView viewDashboard() {
        // Replace with actual dashboard URL
        String dashboardUrl = "localhost:8080/dashboard";
        return new RedirectView(dashboardUrl);
    }

    @GetMapping("/{role}/list")
    public ResponseEntity<List<User>> getUserListByRole(@PathVariable String role) {
        try {
            List<User> users = userService.getUserByUserRole(role);
            if (users != null && !users.isEmpty()) {
                return ResponseEntity.ok(users);
            } else {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getUser")
    public ResponseEntity<User> getUserByUserID(@RequestParam String id) {
        try {
            User user = userService.getUserByUserID(Integer.parseInt(id));
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //Search by name, status, counter ID, contactInfo
    @GetMapping("/{role}/list/searchUser")
    public ResponseEntity<List<User>> searchUser(
            @PathVariable String role,
            @RequestParam String input,
            @RequestParam String filter) {
        try {
            List<User> users = userService.getUserByUserRole(role);
            List<User> searchedUserList = userService.searchUser(input, filter, users);
            if (users != null && !users.isEmpty()) {
                return ResponseEntity.ok(searchedUserList);
            } else {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
