package com.swp391.jewelrysalesystem.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.swp391.jewelrysalesystem.models.CartItem;
import com.swp391.jewelrysalesystem.models.Product;

@Service
public class CartService implements ICartService {
    private List<CartItem> cartItems = new ArrayList<>();

    @Override
    public void addItem(Product product, int quantity, double price) {
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
    public void deleteItem(int productID) {
        Iterator<CartItem> iterator = cartItems.iterator();
        while (iterator.hasNext()) {
            CartItem cartItem = iterator.next();
            if (cartItem.getProduct().getID() == productID) {
                iterator.remove(); // Xóa cartItem khỏi danh sách cartItems
            }
        }
    }

    @Override
    public boolean updateCart(Product product, int newQuantity) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getID() == product.getID()) {
                item.setQuantity(newQuantity);
                return true;
            }
        }
        return false;
    }

    public double totalPriceCal(List<CartItem> cart){
        double totalPrice = 0;
        for (CartItem cartItem : cart) {
            totalPrice += cartItem.getPrice() * cartItem.getQuantity();
        }
        return totalPrice; 
    }
    @Override
    public List<CartItem> viewCart() {
        updatePriceOfEachProduct();
        return cartItems;
    }

    @Override
    public void updatePriceOfEachProduct() {
        for (CartItem cartItem : cartItems) {
            cartItem.setPrice(cartItem.getQuantity() * cartItem.getProduct().getPrice());
        }
    }

    @Override
    public boolean clearCart() {
        cartItems.clear();;
        return true;
    }

}
