package com.swp391.jewelrysalesystem.controllers;

import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.util.concurrent.ExecutionError;
import com.swp391.jewelrysalesystem.services.UserService;

import com.swp391.jewelrysalesystem.models.User;;


@RestController
public class CRUDController {
    public UserService userService;

    @Autowired
    public CRUDController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get")
    public User getUser(@RequestParam String userId) throws InterruptedException, ExecutionError, ExecutionException {
        return userService.getUser(userId);
    }

    //Firebase connection test
    //Retrieve user collection
    @GetMapping("/getUsers")
    public String getUsers() {
        return userService.getUsers();
    }


    //End-point test
    @GetMapping("/test")
    public ResponseEntity<String> testGetEndPoint() {
        return ResponseEntity.ok("Test Get Endpoint is Working");
    }
}
