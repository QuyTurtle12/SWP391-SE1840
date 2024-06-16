package com.swp391.jewelrysalesystem.services;

import java.util.List;

import com.swp391.jewelrysalesystem.models.CartItem;
import com.swp391.jewelrysalesystem.models.Product;

public interface ICartService {
    public void addItem(Product product, int quantity, double price);

     public void deleteItem(int productID);

    public boolean updateCart(Product product, int newQuantity);

    public List<CartItem> viewCart();

    public void updatePriceOfEachProduct();

    public boolean clearCart();
}