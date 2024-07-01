package com.swp391.jewelrysalesystem.services;

import java.util.List;

import com.swp391.jewelrysalesystem.models.Order;
import com.swp391.jewelrysalesystem.models.OrderDTO;

public interface IOrderService {
    List<Order> getOrderList();

    Order getOrder(int ID);

    List<Order> searchOrderList(String input, String filter, List<Order> orderList);

    List<OrderDTO> getOrderDetailList(int orderID);

    boolean saveOrder(Order order);

    boolean saveOrderDTO(OrderDTO orderDTO);

    boolean isNotNullOrder(int ID);

    boolean isNotNullStaff(int ID);

    boolean isNotNullCounter(int ID);

    String isGeneralValidated(int staffID, int counterID, String customerGender, String customerName, double discountApplied);
}
