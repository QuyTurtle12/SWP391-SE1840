package com.swp391.jewelrysalesystem.controllers;

import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import com.swp391.jewelrysalesystem.models.User;
import com.swp391.jewelrysalesystem.services.ICustomerService;
import com.swp391.jewelrysalesystem.services.IUserService;
import com.swp391.jewelrysalesystem.services.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

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
    private ICustomerService customerService;
    private JwtUtil jwtUtil;

    @Autowired
    public UserController(IUserService userService, ICustomerService customerService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.customerService = customerService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("v2/accounts/{role}")
    public ResponseEntity<String> addUserV2(
            @RequestParam String fullName,
            @RequestParam String gender,
            @RequestParam String email,
            @PathVariable String role,
            @RequestParam String password,
            @RequestParam String contactInfo,
            @RequestParam int counterID) {

        if (!userService.isNotExistedPhoneNum(contactInfo) || !customerService.isNotExistedPhoneNum(contactInfo)
                || !userService.isNotExistedEmail(email)) {
            return ResponseEntity.status(HttpStatus.SC_CONFLICT)
                    .body("This user has been existed! Please, check contact info or email");
        }

        if (!isValidEmail(email)) {
            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST)
                    .body("Email must end with @gmail.com or @yahoo.com");
        }

        if (password.isBlank() || password.equals(null)) {
            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("Password cannot be empty!");
        }

        String error = userService.isGeneralValidated(fullName, gender, contactInfo, counterID);
        if (error != null) {
            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(error);
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

        int ID = userService.generateID();

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
            return ResponseEntity.status(HttpStatus.SC_CONFLICT)
                    .body("This user is not existing");
        }

        if ((!customerService.isNotExistedPhoneNum(contactInfo) || !userService.isNotExistedPhoneNum(contactInfo))
                && !userService.isMyPhoneNum(ID, contactInfo)) {
            return ResponseEntity.status(HttpStatus.SC_CONFLICT)
                    .body("This phone number has been registered");
        }

        String error = userService.isGeneralValidated(fullName, gender, contactInfo, counterID);
        if (error != null) {
            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(error);
        }

        User existingUser = userService.getUserByField(ID, "id", "user");

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
                List<User> users = userService.getUserListByField(role, "roleID", "user");
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

    @GetMapping("/this-info")
    public ResponseEntity<User> getThisUser(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization").substring(7);
            int userID = Integer.parseInt(jwtUtil.extractUserID(token));
            User user = userService.getUserByField(userID, "id", "user");
            if (user == null) {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).build();
            }
            return ResponseEntity.ok(user);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/v2/accounts/user")
    public ResponseEntity<User> getUserByUserIDV2(@RequestParam int id) {
        try {
            User user = userService.getUserByField(id, "id", "user");
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
            List<User> users = userService.getUserListByField(role, "roleID", "user");
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
}
