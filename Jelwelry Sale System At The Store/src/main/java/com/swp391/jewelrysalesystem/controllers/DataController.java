package com.swp391.jewelrysalesystem.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

public class DataController {
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin/data")
    public ResponseEntity<String> getAdminData() {
        return ResponseEntity.ok("Data only for Admins");
    }

    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    @GetMapping("/manager/data")
    public ResponseEntity<String> getManagerData() {
        return ResponseEntity.ok("Data for Managers and Admins");
    }

    @PreAuthorize("hasAuthority('STAFF')")
    @GetMapping("/staff/data")
    public ResponseEntity<String> getStaffData() {
        return ResponseEntity.ok("Data only for Staff");
    }

}
