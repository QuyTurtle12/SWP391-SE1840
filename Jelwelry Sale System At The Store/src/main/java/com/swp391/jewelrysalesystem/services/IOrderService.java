package com.swp391.jewelrysalesystem.services;

import java.util.List;

import com.swp391.jewelrysalesystem.models.Order;
import com.swp391.jewelrysalesystem.models.OrderDTO;

public interface IOrderService {
    List<Order> getOrderList();

    Order getOrder(int ID);

    List<Order> searchOrderList(String input, String filter, List<Order> orderList);

    Order saveOrder(Order order);

    OrderDTO saveOrderDTO(OrderDTO orderDTO);

    boolean isNotNullOrder(int ID);
}
