package com.swp391.jewelrysalesystem.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swp391.jewelrysalesystem.models.CartItem;
import com.swp391.jewelrysalesystem.models.Product;
import com.swp391.jewelrysalesystem.services.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public String addItem(@RequestBody Product product, @RequestParam int quantity, @RequestParam double price) {
        cartService.addItem(product, quantity, price);
        return "Item added to cart";
    }

    @DeleteMapping("/delete")
    public String deleteItem(@RequestBody Product product) {
        cartService.deleteItem(product);
        return "Item removed from cart";
    }

    @PostMapping("/update")
    public String updateCart(@RequestBody Product product, @RequestParam int quantity) {
        cartService.updateCart(product, quantity);
        return "Cart updated";
    }

    @GetMapping("/view")
    public List<CartItem> viewCart() {
        return cartService.viewCart();
    }

}
