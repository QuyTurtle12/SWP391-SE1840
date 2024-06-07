package com.swp391.jewelrysalesystem.controllers;

import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.util.concurrent.ExecutionError;
import com.swp391.jewelrysalesystem.services.IUserService;
import com.swp391.jewelrysalesystem.models.User;

@RestController
public class CRUDController {
    public IUserService userService;

    @Autowired
    public CRUDController(IUserService userService) {
        this.userService = userService;
    }

    // Get user detail info
    @GetMapping("/get")
    public User getUser(@RequestParam String userId) throws InterruptedException, ExecutionError, ExecutionException {
        return userService.getUserData(userId);
    }

    // Firebase connection test
    // Retrieve user collection
    @GetMapping("/getUsers")
    public String getUsers() {
        return userService.getUserList();
    }

    // End-point test
    @GetMapping("/test")
    public ResponseEntity<String> testGetEndPoint() {
        return ResponseEntity.ok("Test Get Endpoint is Working");
    }

}
