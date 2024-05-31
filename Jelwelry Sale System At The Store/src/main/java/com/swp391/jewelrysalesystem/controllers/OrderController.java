package com.swp391.jewelrysalesystem.controllers;

import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swp391.jewelrysalesystem.models.Order;
import com.swp391.jewelrysalesystem.services.IOrderService;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/order")
public class OrderController {
    private IOrderService orderService;

    @Autowired
    public OrderController(IOrderService orderService){
        this.orderService = orderService;
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
