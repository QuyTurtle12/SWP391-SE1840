package com.swp391.jewelrysalesystem.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.swp391.jewelrysalesystem.models.CartItem;
import com.swp391.jewelrysalesystem.models.Product;

@Service
public class CartService implements ICartService {
    private Map<String, List<CartItem>> userCarts = new HashMap<>();

    @Override
    public void addItem(String userId, Product product, int quantity, double price) {
        List<CartItem> cartItems = userCarts.computeIfAbsent(userId, k -> new ArrayList<>());
        int count = 0;
        int index = 0;
        for (CartItem cartItem : cartItems) {
            if (product.getID() == cartItem.getProduct().getID()) {
                count = 1;
                break;
            }
            index++;
        }

        if (count == 1) {
            int currentProductQuantity = cartItems.get(index).getQuantity();
            cartItems.get(index).setQuantity(currentProductQuantity + 1);
        } else {
            cartItems.add(new CartItem(product, quantity, price));
        }
    }

    @Override
    public void deleteItem(String userId, int productID) {
        List<CartItem> cartItems = userCarts.get(userId);
        if (cartItems == null) {
            return;
        }

        Iterator<CartItem> iterator = cartItems.iterator();
        while (iterator.hasNext()) {
            CartItem cartItem = iterator.next();
            if (cartItem.getProduct().getID() == productID) {
                iterator.remove();
                break;
            }
        }
    }

    @Override
    public boolean updateCart(String userId, Product product, int newQuantity) {
        List<CartItem> cartItems = userCarts.get(userId);
        if (cartItems == null) {
            return false;
        }
        for (CartItem item : cartItems) {
            if (item.getProduct().getID() == product.getID()) {
                item.setQuantity(newQuantity);
                return true;
            }
        }
        return false;
    }

    public double totalPriceCal(String userId) {
        double totalPrice = 0;
        List<CartItem> cartItems = userCarts.get(userId);
        if (cartItems != null) {

        }
        for (CartItem cartItem : cartItems) {
            totalPrice += cartItem.getPrice() * cartItem.getQuantity();
        }
        return totalPrice;
    }

    @Override
    public List<CartItem> viewCart(String userId) {
        updatePriceOfEachProduct(userId);
        return userCarts.getOrDefault(userId, new ArrayList<>());
    }

    @Override
    public void updatePriceOfEachProduct(String userId) {
        List<CartItem> cartItems = userCarts.get(userId);
        if (cartItems != null) {
            for (CartItem cartItem : cartItems) {
                cartItem.setPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountPrice());
            }
        }
    }

    @Override
    public boolean clearCart(String userId) {
        List<CartItem> cartItems = userCarts.get(userId);
        if (cartItems != null) {
            cartItems.clear();
            return true;
        }
        return false;
    }

    @Override
    public void updateRefundPriceOfEachProduct(String userId) {
        List<CartItem> cartItems = userCarts.get(userId);
        if (cartItems != null) {
            for (CartItem cartItem : cartItems) {
                cartItem.setPrice(cartItem.getQuantity() * cartItem.getProduct().getRefundPrice());
            }
        }
    }

    @Override
    public List<CartItem> viewRefundedCart(String userId) {
        updateRefundPriceOfEachProduct(userId);
        return userCarts.getOrDefault(userId, new ArrayList<>());
    }
}
