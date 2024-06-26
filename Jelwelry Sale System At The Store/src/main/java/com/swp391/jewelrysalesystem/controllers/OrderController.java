package com.swp391.jewelrysalesystem.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.Timestamp;
import com.swp391.jewelrysalesystem.models.CartItem;
import com.swp391.jewelrysalesystem.models.Customer;
import com.swp391.jewelrysalesystem.models.Order;
import com.swp391.jewelrysalesystem.models.OrderDTO;
import com.swp391.jewelrysalesystem.models.Product;
import com.swp391.jewelrysalesystem.services.GenericService;
import com.swp391.jewelrysalesystem.services.ICustomerService;
import com.swp391.jewelrysalesystem.services.IOrderService;
import com.swp391.jewelrysalesystem.services.IProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api")
public class OrderController {
    private IOrderService orderService;
    private IProductService productService;
    private ICustomerService customerService;

    @Autowired
    public OrderController(IOrderService orderService, IProductService productService, ICustomerService customerService){
        this.orderService = orderService;
        this.productService = productService;
        this.customerService = customerService;
    }

    @PostMapping("/v2/orders")
    public ResponseEntity<String> createOrderV2(@RequestBody List<CartItem> cart,
                             @RequestParam double totalPrice,
                             @RequestParam int orderID,
                             @RequestParam int staffID,
                             @RequestParam int counterID,
                             @RequestParam String customerPhone,
                             @RequestParam double discountApplied) {

        if (orderService.isNotNullOrder(orderID)) {
            return ResponseEntity.status(HttpStatus.SC_CONFLICT).body("Order ID " + orderID +" is existing");
        }

        if (!orderService.isNotNullStaff(staffID)) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body("Staff ID " + staffID +" is not existing");
        }

        Customer customer = customerService.getCustomerByPhone(customerPhone);

        if (customer == null) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body("Customer Phone " + customerPhone +" is not existing");
        }

        int customerID = customer.getID();
        
        if (!orderService.isNotNullCounter(counterID)) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body("Counter ID " + counterID +" is not existing");
        }


        Order newOrder = new Order();
        newOrder.setID(orderID);
        newOrder.setDate(Timestamp.now());
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
            for (OrderDTO orderDTO : orderDTOs) {
                Product product = productService.getProductByID(orderDTO.getProductID());
                product.setStock(product.getStock() - orderDTO.getAmount());
                productService.saveProduct(product);
            }
            return ResponseEntity.ok().body("Create order Successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Error creating an order");
        }
    }

    @GetMapping("/v2/orders")
    public ResponseEntity<List<Map<String, Object>>> getOrderListV2() {
        try {
            List<Order> orderList = orderService.getOrderList();

            if (orderList == null || orderList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
            }

            List<Map<String, Object>> responseList = new ArrayList<>();
            for (Order order : orderList) {
                Map<String, Object> orderMap = new HashMap<>();
                orderMap.put("id", order.getID());
                orderMap.put("date", order.getDate());
                orderMap.put("staffID", order.getStaffID());
                orderMap.put("counterID", order.getCounterID());
                orderMap.put("totalPrice", order.getTotalPrice());
                orderMap.put("discountApplied", order.getDiscountApplied());

                String customerName = new GenericService<Customer>().getByField(order.getCustomerID(), "id", "customer", Customer.class).getName();
                orderMap.put("customerName", customerName);

                responseList.add(orderMap);
            }

            return ResponseEntity.ok(responseList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }

    
    @GetMapping("/v2/orders/order")
    public ResponseEntity<Order> getOrderV2(@RequestParam int id) {
        try {
            Order order = orderService.getOrder(id);
            
            if (order == null) {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
            }

            return ResponseEntity.ok(order);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/v2/orders/order/products")
    public ResponseEntity<List<Map<String, Object>>> getOrderDetailV2(@RequestParam int id) {
        try {
            List<OrderDTO> orders = orderService.getOrderDetailList(id);

            if (orders == null) {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
            }            

            List<Map<String, Object>> responseList = new ArrayList<>();
            for (OrderDTO orderDetail : orders) {
                String productName = productService.getProductByID(orderDetail.getProductID()).getName();
                Map<String, Object> orderDetailMap = new HashMap<>();
                orderDetailMap.put("productName", productName);
                orderDetailMap.put("amount", orderDetail.getAmount());

                responseList.add(orderDetailMap);
            }

            return ResponseEntity.ok(responseList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/v2/orders/search")
    public ResponseEntity<List<Order>> searchOrderListV2(
        @RequestParam String input,
        @RequestParam String filter) {
        try {
            List<Order> orderList = orderService.searchOrderList(input, filter, orderService.getOrderList());

            if (orderList == null && orderList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
                
            }

            return ResponseEntity.ok(orderList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }

    
    //Old endpoints version are below here
    @PostMapping("/order/createOrder")
    public String createOrder(@RequestBody List<CartItem> cart,
                             @RequestParam double totalPrice,
                             @RequestParam int orderID,
                             @RequestParam int staffID,
                             @RequestParam int counterID,
                             @RequestParam int customerID,
                             @RequestParam double discountApplied) {
        Order newOrder = new Order();
        newOrder.setID(orderID);
        newOrder.setDate(Timestamp.now());
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
    

    @GetMapping("/order/list")
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
    
    @GetMapping("/order/get")
    public ResponseEntity<Order> getOrder(@RequestParam int id) {
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

    @GetMapping("/order/list/search")
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
