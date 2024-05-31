package com.swp391.jewelrysalesystem.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.swp391.jewelrysalesystem.models.Product;
import com.swp391.jewelrysalesystem.services.IProductService;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final IProductService productService;

    @Autowired
    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(
        @RequestParam int ID,
        @RequestParam String name,
        @RequestParam double price,
        @RequestParam double refundPrice,
        @RequestParam String description,
        @RequestParam double goldWeight,
        @RequestParam double laborCost,
        @RequestParam double stoneCost,
        @RequestParam int stock,
        @RequestParam String category,
        @RequestParam int promotionID) {

        try {
            Product newProduct = new Product(ID, name, price, refundPrice, description, goldWeight, laborCost, stoneCost, stock, promotionID, category, true);
            Product savedProduct = productService.saveProduct(newProduct);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{ID}/update-info")
    public ResponseEntity<Product> updateProductInfo(
        @PathVariable int ID,
        @RequestParam String name,
        @RequestParam double price,
        @RequestParam double refundPrice,
        @RequestParam String description,
        @RequestParam double goldWeight,
        @RequestParam double laborCost,
        @RequestParam double stoneCost,
        @RequestParam int stock,
        @RequestParam String category,
        @RequestParam int promotionID) {

        try {
            Product existingProduct = productService.getProductByID(ID);
            if (existingProduct == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            existingProduct.setName(name);
            existingProduct.setPrice(price);
            existingProduct.setRefundPrice(refundPrice);
            existingProduct.setDescription(description);
            existingProduct.setGoldWeight(goldWeight);
            existingProduct.setLaborCost(laborCost);
            existingProduct.setStoneCost(stoneCost);
            existingProduct.setStock(stock);
            existingProduct.setCategory(category);
            existingProduct.setPromotionID(promotionID);

            Product updatedProduct = productService.saveProduct(existingProduct);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<Product>> getProductList() {
        try {
            List<Product> productList = productService.getProductList();
            if (productList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(productList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{ID}/info")
    public ResponseEntity<Product> getProduct(@PathVariable int ID) {
        try {
            Product product = productService.getProductByID(ID);
            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{ID}/change-status")
    public ResponseEntity<Product> changeProductStatus(@PathVariable int ID) {
        try {
            Product product = productService.changeProductStatus(ID);
            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/list/search")
    public ResponseEntity<List<Product>> searchProduct(
        @RequestParam String input,
        @RequestParam String filter) {

        try {
            List<Product> productList = productService.searchProduct(input, filter, productService.getProductList());
            if (productList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(productList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/list/sort")
    public ResponseEntity<List<Product>> sortProductList(
        @RequestParam String filter,
        @RequestParam String sortOrder) {

        try {
            List<Product> productList = productService.sortProduct(filter, sortOrder, productService.getProductList());
            if (productList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(productList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/list/search/sort")
    public ResponseEntity<List<Product>> sortSearchedProduct(
        @RequestParam String input,
        @RequestParam String filter,
        @RequestParam String sortFilter,
        @RequestParam String sortOrder) {

        try {
            List<Product> productList = productService.searchProduct(input, filter, productService.getProductList());
            productList = productService.sortProduct(sortFilter, sortOrder, productList);
            if (productList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(productList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
