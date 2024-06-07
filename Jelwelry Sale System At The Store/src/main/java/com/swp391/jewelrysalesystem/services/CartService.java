package com.swp391.jewelrysalesystem.services;

import java.util.ArrayList;
import java.util.List;

import com.swp391.jewelrysalesystem.models.CartItem;
import com.swp391.jewelrysalesystem.models.Product;

public class CartService implements ICartService {
    private List<CartItem> cartItems = new ArrayList<>();

    @Override
    public void addItem(Product product, int quantity, double price) {
        cartItems.add(new CartItem(product, quantity, price));
    }

    @Override
    public void deleteItem(Product product) {
        cartItems.removeIf(item -> item.getProduct().getID() == product.getID());
    }

    @Override
    public void updateCart(Product product, int newQuantity) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getID() == product.getID()) {
                item.setQuantity(newQuantity);
                break;
            }
        }
    }

    @Override
    public List<CartItem> viewCart() {
        return cartItems;
    }

}
