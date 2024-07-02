package com.swp391.jewelrysalesystem.services;

import java.util.List;

import com.swp391.jewelrysalesystem.models.CartItem;
import com.swp391.jewelrysalesystem.models.Product;

public interface ICartService {
    void addItem(String userId, Product product, int quantity, double price);

    void deleteItem(String userId, int productID);

    boolean updateCart(String userId, Product product, int newQuantity);

    List<CartItem> viewCart(String userId);

    List<CartItem> viewRefundedCart(String userId);

    void updatePriceOfEachProduct(String userId);

    void updateRefundPriceOfEachProduct(String userId);

    boolean clearCart(String userId);
}