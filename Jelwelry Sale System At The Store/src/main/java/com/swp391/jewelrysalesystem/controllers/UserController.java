package com.swp391.jewelrysalesystem.controllers;

import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/api")
public class UserController {

    private IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("v2/accounts/{role}")
    public ResponseEntity<String> addUserV2(
            @RequestParam int ID,
            @RequestParam String fullName,
            @RequestParam String gender,
            @RequestParam String email,
            @PathVariable String role,
            @RequestParam String password,
            @RequestParam String contactInfo,
            @RequestParam int counterID) {

        if (contactInfo.length() < 10 || contactInfo.length() > 11) {
            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("Phone Number must be in range 10 - 11");
        }
        if (userService.isNotNullUser(ID) || !userService.isNotExistedPhoneNum(ID, contactInfo)) {
            return ResponseEntity.status(HttpStatus.SC_CONFLICT)
                    .body("This user has been existed! Please, check ID or contact info");
        }

        if (fullName.isBlank() || fullName.equals(null)) {
            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("Full name cannot be empty!");
        }

        if (!gender.equals("Male") && !gender.equals("Female") && !gender.equals("Other")) {
            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("Incorrect gender format.");
        }

        if (!isValidEmail(email)) {
            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST)
                    .body("Email must end with @gmail.com or @yahoo.com");
        }

        if (password.isBlank() || password.equals(null)) {
            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("Password cannot be empty!");
        }

        if (contactInfo.isBlank() || contactInfo.equals(null)) {
            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("Contact Info cannot be empty!");
        }

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

        return userService.registerUser(newUser) ? ResponseEntity.status(HttpStatus.SC_CREATED).build()
                : ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Error saving a user!");
    }

    @PutMapping("v2/accounts/{role}")
    public ResponseEntity<String> updateUserInfoV2(
            @RequestParam int ID,
            @RequestParam String fullName,
            @RequestParam String gender,
            @RequestParam String contactInfo,
            @RequestParam int counterID)
            throws InterruptedException, ExecutionException {

        if (!userService.isNotNullUser(ID)) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body("This user does not exist!");
        }

        if (fullName.isBlank() || fullName.equals(null)) {
            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("Full name cannot be empty!");
        }

        if (!gender.equals("Male") && !gender.equals("Female") && !gender.equals("Other")) {
            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("Incorrect gender format.");
        }

        if (contactInfo.isBlank() || contactInfo.equals(null)) {
            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("Contact Info cannot be empty!");
        }

        if (contactInfo.length() < 10 || contactInfo.length() > 11) {
            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("Phone Number must be in range 10 - 11");
        }

        if (!userService.isNotExistedPhoneNum(ID, contactInfo)) {
            return ResponseEntity.status(HttpStatus.SC_CONFLICT).body("This contact info has been existed!");
        }

        User existingUser = userService.getUserByUserID(ID);

        existingUser.setFullName(fullName);
        existingUser.setGender(gender);
        existingUser.setContactInfo(contactInfo);
        existingUser.setCounterID(counterID);

        return userService.saveUser(existingUser) ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Error saving a user!");
    }

    @PutMapping("v2/accounts/{role}/status")
    public ResponseEntity<String> changeUserStatusV2(@RequestParam int ID) {
        if (!userService.isNotNullUser(ID)) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body("This user does not exist!");
        }

        return userService.changeUserStatus(ID) ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Error changing user status");
    }

    @GetMapping("v2/accounts/dashboard")
    public RedirectView viewDashboardV2() {
        String dashboardUrl = "localhost:8080/dashboard";
        return new RedirectView(dashboardUrl);
    }

    @GetMapping("v2/accounts/{role}")
    public ResponseEntity<List<User>> getUserListByRoleV2(@PathVariable String role) {
        try {
            if (role.equals("MANAGER") || role.equals("STAFF")) {
                List<User> users = userService.getUserByUserRole(role);
                if (users == null && users.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).build();
                }
                return ResponseEntity.ok(users);
            } else {
                return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).build();
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("v2/accounts/user")
    public ResponseEntity<User> getUserByUserIDV2(@RequestParam String id) {
        try {
            User user = userService.getUserByUserID(Integer.parseInt(id));
            if (user == null) {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).build();
            }
            return ResponseEntity.ok(user);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
        }
    }

    // Search by name, status, counter ID, contactInfo
    @GetMapping("v2/accounts/{role}/search")
    public ResponseEntity<List<User>> searchUserV2(
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

    private boolean isValidEmail(String email) {
        // Regular expression to check if email ends with @gmail.com or @yahoo.com
        String emailPattern = "^[\\w-\\.]+@(gmail\\.com|yahoo\\.com)$";
        return Pattern.compile(emailPattern).matcher(email).matches();
    }

    // Old endpoints version below here
    @PostMapping("/account/{role}/register")
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

        userService.saveUser(newUser);
        return null;
    }

    @PutMapping("/account/{role}/update-info")
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

            userService.saveUser(existingUser);
            return null;
        } else {
            throw new RuntimeException("User with ID " + ID + " not found.");
        }
    }

    @PutMapping("/account/{role}/change-status")
    public boolean changeUserStatus(@RequestParam int ID) {
        return userService.changeUserStatus(ID);
    }

    @GetMapping("/account/dashboard")
    public RedirectView viewDashboard() {
        // Replace with actual dashboard URL
        String dashboardUrl = "localhost:8080/dashboard";
        return new RedirectView(dashboardUrl);
    }

    @GetMapping("/account/{role}/list")
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

    @GetMapping("/account/getUser")
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

    // Search by name, status, counter ID, contactInfo
    @GetMapping("/account/{role}/list/searchUser")
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
