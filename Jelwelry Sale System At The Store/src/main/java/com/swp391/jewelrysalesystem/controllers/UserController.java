package com.swp391.jewelrysalesystem.controllers;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.swp391.jewelrysalesystem.models.User;
import com.swp391.jewelrysalesystem.services.IUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/api/account")
public class UserController {

    private IUserService userService;

    @Autowired
    public UserController(IUserService userService){
        this.userService = userService;
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
