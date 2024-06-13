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
import com.swp391.jewelrysalesystem.services.ICartService;

@RestController
@RequestMapping("/cart")
public class CartController {
    private ICartService cartService;

    @Autowired
    public CartController(ICartService cartService){
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public String addItem(@RequestBody Product product, @RequestParam int quantity, @RequestParam double price) {
        if (product.getStock() > 0) {
            if (quantity <= product.getStock()) {
                cartService.addItem(product, quantity, price);
                return "Item added to cart";
            } else {
                return "Not enough quantity in stock";
            }
        } else {
            return "This item is out of stock";
        }
    }

    @DeleteMapping("/delete")
    public String deleteItem(@RequestBody Product product) {
        cartService.deleteItem(product);
        return "Item removed from cart";
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
