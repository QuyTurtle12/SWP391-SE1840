package com.swp391.jewelrysalesystem.controllers;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swp391.jewelrysalesystem.models.CartItem;
import com.swp391.jewelrysalesystem.models.Product;
import com.swp391.jewelrysalesystem.models.User;
import com.swp391.jewelrysalesystem.services.ICartService;
import com.swp391.jewelrysalesystem.services.IProductService;
import com.swp391.jewelrysalesystem.services.IUserService;

@RestController
@RequestMapping("/cart")
public class CartController {
    private ICartService cartService;
    private IProductService productService;
    private IUserService userService;

    @Autowired
    public CartController(ICartService cartService, IProductService productService, IUserService userService) {
        this.cartService = cartService;
        this.productService = productService;
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity<String> addItemV2(@RequestParam int staffId, @RequestBody Product product) {
        int quantity = 1;
        double price = product.getPrice();

        if (product.getStock() <= 0) {
            return ResponseEntity.badRequest().body("This item is out of stock");
        }

        if (quantity > product.getStock()) {
            return ResponseEntity.badRequest().body("Not enough quantity in stock");
        }

        try {
            User staff = userService.getUserByField(staffId, "id", "user");
            if (staff == null || staff.getRoleID() != 1) {
                return ResponseEntity.badRequest().body("Staff ID " + staffId + " is not existing!");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error retrieving staff info");
        }

        cartService.addItem(String.valueOf(staffId), product, quantity, price);
        return ResponseEntity.ok().body("Item added to cart");
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteItemV2(@RequestParam int staffId, @RequestParam int productID) {
        try {
            User staff = userService.getUserByField(staffId, "id", "user");
            if (staff == null || staff.getRoleID() != 1) {
                return ResponseEntity.badRequest().body("Staff ID " + staffId + " is not existing!");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error retrieving staff info");
        }

        cartService.deleteItem(String.valueOf(staffId), productID);
        return ResponseEntity.ok().body("Item removed from cart");
    }

    @PutMapping("")
    public ResponseEntity<String> updateCartV2(@RequestParam int staffId, @RequestParam int productID,
            @RequestParam int quantity) {
        try {
            User staff = userService.getUserByField(staffId, "id", "user");
            if (staff == null || staff.getRoleID() != 1) {
                return ResponseEntity.badRequest().body("Staff ID " + staffId + " is not existing!");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error retrieving staff info");
        }

        Product product = null;
        try {
            product = productService.getProductByID(productID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (quantity > product.getStock()) {
            return ResponseEntity.badRequest().body("Not enough quantity in stock");
        }

        if (cartService.updateCart(String.valueOf(staffId), product, quantity)) {
            return ResponseEntity.ok().body("Cart updated");
        }

        return ResponseEntity.badRequest().body("Error updating cart");
    }

    @GetMapping("")
    public List<CartItem> viewCartV2(@RequestParam int staffId) {
        return cartService.viewCart(String.valueOf(staffId));
    }

    @PutMapping("/clear")
    public ResponseEntity<String> clearCart(@RequestParam int staffId) {
        return cartService.clearCart(String.valueOf(staffId)) ? ResponseEntity.ok().body("Clear Cart Successfully")
                : ResponseEntity.internalServerError().body("Error clearing cart");
    }

    // Refund Cart
    @PostMapping("/refundItem")
    public ResponseEntity<String> addRefundedItem(@RequestParam int staffId, @RequestBody Product product) {
        try {
            User staff = userService.getUserByField(staffId, "id", "user");
            if (staff == null || staff.getRoleID() != 1) {
                return ResponseEntity.badRequest().body("Staff ID " + staffId + " is not existing!");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error retrieving staff info");
        }
        
        int quantity = 1;
        double price = product.getRefundPrice();
        cartService.addItem(String.valueOf(staffId), product, quantity, price);
        return ResponseEntity.ok().body("add cart successfully");
    }

    @GetMapping("/refundItem")
    public List<CartItem> viewRefundedCartV2(@RequestParam int staffId) {
        return cartService.viewRefundedCart(String.valueOf(staffId));
    }
}
