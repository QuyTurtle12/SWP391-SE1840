package com.swp391.jewelrysalesystem.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swp391.jewelrysalesystem.models.Category;
import com.swp391.jewelrysalesystem.services.ICategory;

import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;




@RestController
@RequestMapping("/api")
public class CategoryController {

    private final ICategory categoryService;

    @Autowired
    public CategoryController(ICategory categoryService){
        this.categoryService = categoryService;
    }

    @PostMapping("/categories")
    public ResponseEntity<String> addCategory(@RequestParam int ID, @RequestParam String categoryName) {
        if (categoryService.isNotNullCategory(ID)) {
            return ResponseEntity.status(HttpStatus.SC_CONFLICT).body("This ID " + ID + " has been existed");
        }

        Category category = new Category(ID, categoryName);

        return categoryService.saveCatogory(category) 
        ? ResponseEntity.status(HttpStatus.SC_CREATED).body("Create Successfully")
        : ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Error creating category");
    }

    @PutMapping("/categories")
    public ResponseEntity<String> updateCategory(@RequestParam int ID, @RequestParam String categoryName) {
        if (!categoryService.isNotNullCategory(ID)) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body("This ID " + ID + " is not existing");
        }

        Category category = new Category(ID, categoryName);

        return categoryService.saveCatogory(category) 
        ? ResponseEntity.status(HttpStatus.SC_CREATED).body("Updating Successfully")
        : ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Error updating category");
    }
    
    @DeleteMapping("/categories")
    public ResponseEntity<String> deleteCategory(@RequestParam int ID){
        if (!categoryService.isNotNullCategory(ID)) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body("This ID " + ID + " is not existing");
        }

        return categoryService.removeCategory(ID) 
        ? ResponseEntity.ok().body("Deleting Successfully")
        : ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Error deleting category");
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategoryList() {
        List<Category> categories = categoryService.getCategoryList();
        if (categories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).build();
        }
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/categories/category")
    public ResponseEntity<Category> getCategory(@RequestParam int ID) {
        Category category = categoryService.getCategory(ID);

        if (category == null) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).build();
        }

        return ResponseEntity.ok(category);
    }
    
}
