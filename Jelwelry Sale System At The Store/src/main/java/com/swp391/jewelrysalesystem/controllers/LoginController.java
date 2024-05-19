package com.swp391.jewelrysalesystem.controllers;

import com.google.common.util.concurrent.ExecutionError;
import com.swp391.jewelrysalesystem.models.LoginRequest;
import com.swp391.jewelrysalesystem.models.User;
import com.swp391.jewelrysalesystem.services.IUserService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
public class LoginController {
    public IUserService userService;

    @Autowired
    public LoginController(IUserService userService){
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest) throws InterruptedException, ExecutionError {
        try {
            String idToken = loginRequest.getIdToken();
            String uid = userService.login(idToken);
            if (uid != null) {
                User userData = userService.getUserData(uid);
                if (userData != null) {
                    return ResponseEntity.ok(userData);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
