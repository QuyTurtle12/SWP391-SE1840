package com.swp391.jewelrysalesystem.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swp391.jewelrysalesystem.models.CartItem;
import com.swp391.jewelrysalesystem.models.Order;
import com.swp391.jewelrysalesystem.models.OrderDTO;
import com.swp391.jewelrysalesystem.models.Product;
import com.swp391.jewelrysalesystem.services.IOrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/api/order")
public class OrderController {
    private IOrderService orderService;

    @Autowired
    public OrderController(IOrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping("/createOrder")
    public String createOrder(@RequestBody List<CartItem> cart,
                             @RequestParam double totalPrice,
                             @RequestParam int orderID,
                             @RequestParam int staffID,
                             @RequestParam int counterID,
                             @RequestParam int customerID,
                             @RequestParam double discountApplied) {
        Order newOrder = new Order();
        newOrder.setID(orderID);
        //newOrder.setDate(LocalDateTime.now());
        newOrder.setDate(null);
        newOrder.setStaffID(staffID);
        newOrder.setCounterID(counterID);
        newOrder.setCustomerID(customerID);
        newOrder.setTotalPrice(totalPrice);
        newOrder.setDiscountApplied(discountApplied);
        try {
            orderService.saveOrder(newOrder);
            List<OrderDTO> orderDTOs = new ArrayList<>();
            for (CartItem item : cart) {
                OrderDTO orderDTO = new OrderDTO();
                orderDTO.setOrderID(newOrder.getID());
                orderDTO.setProductID(item.getProduct().getID());
                orderDTO.setAmount(item.getQuantity());
                orderDTOs.add(orderDTO);
                orderService.saveOrderDTO(orderDTO);
            }
            return "Create order Successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error creating an order";
        }
    }
    

    @GetMapping("/list")
    public ResponseEntity<List<Order>> getOrderList() {
        try {
            List<Order> orderList = orderService.getOrderList();

            if (orderList != null && !orderList.isEmpty()) {
                return ResponseEntity.ok(orderList);
            } else {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @GetMapping("/get")
    public ResponseEntity<Order> getOrderList(@RequestParam int id) {
        try {
            Order order = orderService.getOrder(id);

            if (order != null) {
                return ResponseEntity.ok(order);
            } else {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/list/search")
    public ResponseEntity<List<Order>> searchOrderList(
        @RequestParam String input,
        @RequestParam String filter) {
        try {
            List<Order> orderList = orderService.searchOrderList(input, filter, orderService.getOrderList());

            if (orderList != null && !orderList.isEmpty()) {
                return ResponseEntity.ok(orderList);
            } else {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
