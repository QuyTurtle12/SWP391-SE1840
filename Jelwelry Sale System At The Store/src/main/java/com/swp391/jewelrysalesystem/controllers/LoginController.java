package com.swp391.jewelrysalesystem.controllers;

import com.google.cloud.storage.Acl.User;
import com.google.common.util.concurrent.ExecutionError;
import com.swp391.jewelrysalesystem.services.UserService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
public class LoginController {
    public UserService userService;

    // @Autowired
    // public LoginController(UserService userService){
    //     this.userService = userService;
    // }

    // @PostMapping("/login")
    // public String login(@RequestBody User user) throws InterruptedException, ExecutionError {
    //     return userService.login(user);
    // }
}
