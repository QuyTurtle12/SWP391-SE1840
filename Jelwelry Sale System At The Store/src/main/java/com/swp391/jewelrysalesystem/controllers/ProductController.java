package com.swp391.jewelrysalesystem.controllers;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swp391.jewelrysalesystem.models.Product;
import com.swp391.jewelrysalesystem.services.IProductService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/api/product")
public class ProductController {

    private IProductService productService;

    @Autowired
    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/add")
    public Product addProduct(
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
        
        Product newProduct = new Product();
        newProduct.setID(ID);
        newProduct.setName(name);
        newProduct.setPrice(price);
        newProduct.setRefundPrice(refundPrice);
        newProduct.setDescription(description);
        newProduct.setGoldWeight(goldWeight);
        newProduct.setLaborCost(laborCost);
        newProduct.setStoneCost(stoneCost);
        newProduct.setStock(stock);
        newProduct.setCategory(category);
        newProduct.setStatus(true);
        newProduct.setPromotionID(promotionID);
        
        return productService.saveProduct(newProduct);
    }

    @PutMapping("/{ID}/update-info")
    public Product updateProductInfo(
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
        @RequestParam int promotionID)
            throws InterruptedException, ExecutionException {

        Product existingProduct = productService.getProductByID(ID);

        if (existingProduct != null) {
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

            return productService.saveProduct(existingProduct);
        } else {
            throw new RuntimeException("Product with ID " + ID + " not found.");
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<Product>> getProductList(){
        try {

            List<Product> productList = productService.getProductList();
            if (!productList.isEmpty()) {
                return ResponseEntity.ok(productList);
            } else {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @GetMapping("/{ID}/info")
    public ResponseEntity<Product> getProduct(@PathVariable int ID) {
        try {
            Product product = productService.getProductByID(ID);
            if (product != null) {
                return ResponseEntity.ok(product);
            } else {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{ID}/change-status") 
    public Product changeProductStatus(@PathVariable int ID) throws InterruptedException, ExecutionException {
        return productService.changeProductStatus(ID);
    }

    //Search by name, status, or category
    @GetMapping("/list/search")
    public ResponseEntity<List<Product>> searchProduct(
        @RequestParam String input, 
        @RequestParam String filter) {
        try {
            List<Product> productList = productService.getProductList();
            productList = productService.searchProduct(input, filter, productList);

            if (!productList.isEmpty()) {
                return ResponseEntity.ok(productList);
            } else {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    //Sort by Alphabet, or Price
    //Sort Order are asc (stand for ascending), desc (stand for descending)
    @GetMapping("/list/sort")
    public ResponseEntity<List<Product>> sortProductList(
        @RequestParam String filter,
        @RequestParam String sortOrder) {
        try {
            List<Product> productList = productService.getProductList();
            productList = productService.sortProduct(filter, sortOrder, productList);

            if (!productList.isEmpty()) {
                return ResponseEntity.ok(productList);
            } else {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
            }
        } catch (InterruptedException | ExecutionException e) {
            
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }

    }
    
    //Search by name, status, or category
    //Sort by Alphabet, or Price
    //Sort Order are asc (stand for ascending), desc (stand for descending)
    @GetMapping("/list/search/sort")
    public ResponseEntity<List<Product>> sortSearchedProduct(
        @RequestParam String input, 
        @RequestParam String filter,
        @RequestParam String sortFilter,
        @RequestParam String sortOrder) {
        try {
            List<Product> productList = productService.getProductList();
            productList = productService.searchProduct(input, filter, productList);

            productList = productService.sortProduct(sortFilter, sortOrder, productList);
            if (!productList.isEmpty()) {
                return ResponseEntity.ok(productList);
            } else {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
}
