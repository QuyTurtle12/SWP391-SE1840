package com.swp391.jewelrysalesystem.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.swp391.jewelrysalesystem.services.ProductService;

import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/cart")
public class CartController {
    private ICartService cartService;

    @Autowired
    public CartController(ICartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("")
    public String addItemV2(@RequestBody Product product) {
        int quantity = 1;
        double price = product.getPrice();
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

    @DeleteMapping("")
    public String deleteItemV2(@RequestParam int productID) {
        cartService.deleteItem(productID);
        return "Item removed from cart";
    }

    @PutMapping("")
    public String updateCartV2(@RequestParam int productID, @RequestParam int quantity) {

        Product product = null;
        try {
            product = new ProductService().getProductByID(productID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (quantity > product.getStock()) {
            return "Not enough quantity in stock";
        }

        if (cartService.updateCart(product, quantity)) {
            return "Cart updated";
        }

        return "Error updating cart";
    }

    @GetMapping("")
    public List<CartItem> viewCartV2() {
        return cartService.viewCart();
    }

    @PutMapping("/clear")
    public String clearCart() {
        return cartService.clearCart() ? "Clear Cart Successfully" : "Error clearing cart";
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
