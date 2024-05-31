package com.swp391.jewelrysalesystem.services;

import java.util.List;

import com.swp391.jewelrysalesystem.models.Order;

public interface IOrderService {
    List<Order> getOrderList();

    Order getOrder(int ID);

    List<Order> searchOrderList(String input, String filter, List<Order> orderList);

    Order saveOrder(Order order);


}
