package com.swp391.jewelrysalesystem.controllers;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import com.swp391.jewelrysalesystem.services.IUserService;
import com.swp391.jewelrysalesystem.models.*;;

@RestController
@RequestMapping("/api/account/manager")
public class UserController {

    @Autowired
    private IUserService userService;

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

}
