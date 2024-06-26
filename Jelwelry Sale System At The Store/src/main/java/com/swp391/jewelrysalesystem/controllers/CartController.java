package com.swp391.jewelrysalesystem.controllers;

import java.util.List;

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
import com.swp391.jewelrysalesystem.services.ICartService;
import com.swp391.jewelrysalesystem.services.IProductService;

@RestController
@RequestMapping("/cart")
public class CartController {
    private ICartService cartService;
    private IProductService productService;

    @Autowired
    public CartController(ICartService cartService, IProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    @PostMapping("")
    public ResponseEntity<String> addItemV2(@RequestBody Product product) {
        int quantity = 1;
        double price = product.getPrice();
        if (product.getStock() <= 0) {
            return ResponseEntity.badRequest().body("This item is out of stock");
        }

        if (quantity > product.getStock()) {
            return ResponseEntity.badRequest().body("Not enough quantity in stock");
        }

        cartService.addItem(product, quantity, price);
        return ResponseEntity.ok().body("Item added to cart");
    }

    @DeleteMapping("")
    public String deleteItemV2(@RequestParam int productID) {
        cartService.deleteItem(productID);
        return "Item removed from cart";
    }

    @PutMapping("")
    public ResponseEntity<String> updateCartV2(@RequestParam int productID, @RequestParam int quantity) {

        Product product = null;
        try {
            product = productService.getProductByID(productID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (quantity > product.getStock()) {
            return ResponseEntity.badRequest().body("Not enough quantity in stock");
        }

        if (cartService.updateCart(product, quantity)) {
            return ResponseEntity.ok().body("Cart updated");
        }

        return ResponseEntity.badRequest().body("Error updating cart");
    }

    @GetMapping("")
    public List<CartItem> viewCartV2() {
        return cartService.viewCart();
    }

    @PutMapping("/clear")
    public ResponseEntity<String> clearCart() {
        return cartService.clearCart() ? ResponseEntity.ok().body("Clear Cart Successfully")
                : ResponseEntity.internalServerError().body("Error clearing cart");
    }

    // Refund Cart
    @PostMapping("/refundItem")
    public String addRefundedItem(@RequestBody Product product) {
        int quantity = 1;
        double price = product.getRefundPrice();
        cartService.addItem(product, quantity, price);
        return "add cart successfully";
    }

    @GetMapping("/refundItem")
    public List<CartItem> viewRefundedCartV2() {
        return cartService.viewRefundedCart();
    }

    // Old endpoints version below here

    @PostMapping("/add")
    public String addItem(@RequestBody Product product, @RequestParam int quantity, @RequestParam double price) {
        if (product.getStock() < 0) {
            return "This item is out of stock";
        }

        if (quantity > product.getStock()) {
            return "Not enough quantity in stock";
        }

        cartService.addItem(product, quantity, price);
        return "Item added to cart";
    }

    @PostMapping("/update")
    public String updateCart(@RequestBody Product product, @RequestParam int quantity) {
        if (quantity <= product.getStock()) {
            cartService.updateCart(product, quantity);
            return "Cart updated";
        } else {
            return "Not enough quantity in stock";
        }
    }

    @GetMapping("/view")
    public List<CartItem> viewCart() {
        return cartService.viewCart();
    }

}
