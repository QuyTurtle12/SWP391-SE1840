package com.swp391.jewelrysalesystem.controllers;

import java.util.concurrent.ExecutionException;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.concurrent.ExecutionException;

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
    public UserController(IUserService userService){
        this.userService = userService;
    }

     @PostMapping("/register")
    public User addManager(
            @RequestParam int ID,
            @RequestParam String fullName,
            @RequestParam String gender,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String contactInfo,
            @RequestParam int counterID) {

        User newManager = new User();
        newManager.setID(ID);
        newManager.setFullName(fullName);
        newManager.setEmail(email);
        newManager.setPassword(password);
        newManager.setGender(gender);
        newManager.setContactInfo(contactInfo);
        newManager.setCounterID(counterID);
        newManager.setRole("MANAGER");
        newManager.setStatus(true);

        return userService.saveUser(newManager);
    }

        @PutMapping("/update-info")
    public User updateManagerInfo(
            @RequestParam int ID,
            @RequestParam String fullName,
            @RequestParam String gender,
            @RequestParam String contactInfo,
            @RequestParam int counterID)
            throws InterruptedException, ExecutionException {

        User existingManager = userService.getUserData(String.valueOf(ID));

        if (existingManager != null) {
            existingManager.setFullName(fullName);
            existingManager.setGender(gender);
            existingManager.setContactInfo(contactInfo);
            existingManager.setCounterID(counterID);

            return userService.saveUser(existingManager);
        } else {
            throw new RuntimeException("Manager with ID " + ID + " not found.");
        }
    }

    @PutMapping("/change-status")
    public User changeManagerStatus(@RequestParam int ID) {
        return userService.changeManagerStatus(ID);
    }

    @GetMapping("/dashboard")
    public RedirectView viewDashboard() {
        // Replace with actual dashboard URL
        String dashboardUrl = "localhost:8080/dashboard";
        return new RedirectView(dashboardUrl);
    }
    
    @GetMapping("/list/role/{role}")
    public ResponseEntity<List<User>> getUserListByRole(@PathVariable String role) {
        try {
            List<User> users = userService.getUserByUserRole(role);
            if (users !=null && !users.isEmpty()) {
                return ResponseEntity.ok(users);
            } else{
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserByUserID(@PathVariable String id) {
        try {
            User user = userService.getUserByUserID(Integer.parseInt(id));
            if (user != null) {
                return ResponseEntity.ok(user);
            } else{
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
}
