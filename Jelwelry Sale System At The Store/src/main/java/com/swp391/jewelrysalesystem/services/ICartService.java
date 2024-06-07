package com.swp391.jewelrysalesystem.services;

import java.util.List;

import com.swp391.jewelrysalesystem.models.CartItem;
import com.swp391.jewelrysalesystem.models.Product;

public interface ICartService {
    public void addItem(Product product, int quantity, double price);

    public void deleteItem(Product product);

    public void updateCart(Product product, int newQuantity);

    public List<CartItem> viewCart();
}