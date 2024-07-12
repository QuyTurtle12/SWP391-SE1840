package com.swp391.jewelrysalesystem.controllers;

import com.swp391.jewelrysalesystem.models.Product;
import com.swp391.jewelrysalesystem.services.FirestoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/firestore")
public class FirestoreController {

    @Autowired
    private FirestoreService firestoreService;

    @PostMapping("/bulk-add")
    public String bulkAddData(@RequestParam String collectionName, @RequestBody List<Product> dataList) {
        try {
            firestoreService.bulkAddData(collectionName, dataList);
            return "Data added successfully.";
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return "Error adding data: " + e.getMessage();
        }
    }
}
