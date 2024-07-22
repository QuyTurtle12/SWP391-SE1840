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
import com.swp391.jewelrysalesystem.models.User;
import com.swp391.jewelrysalesystem.services.ICustomerPromotion;
import com.swp391.jewelrysalesystem.services.ICustomerService;
import com.swp391.jewelrysalesystem.services.IOrderService;
import com.swp391.jewelrysalesystem.services.IProductService;
import com.swp391.jewelrysalesystem.services.IUserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
public class OrderController {
    private IOrderService orderService;
    private IProductService productService;
    private ICustomerService customerService;
    private IUserService userService;
    private ICustomerPromotion customerPromotionService;

    private final int ACCEPTABLE_TOTAL_PRICE = 100;

    @Autowired
    public OrderController(IOrderService orderService, IProductService productService, ICustomerService customerService,
            IUserService userService, ICustomerPromotion customerPromotionService) {
        this.orderService = orderService;
        this.productService = productService;
        this.customerService = customerService;
        this.userService = userService;
        this.customerPromotionService = customerPromotionService;
    }

    @PostMapping("/v2/orders")
    public ResponseEntity<String> createOrderV2(@RequestBody List<CartItem> cart,
            @RequestParam double totalPrice,
            @RequestParam int staffID,
            @RequestParam int counterID,
            @RequestParam String customerPhone,
            @RequestParam String customerGender,
            @RequestParam String customerName,
            @RequestParam String discountName,
            @RequestParam double discountRate,
            @RequestParam int pointApplied) {

        String error = orderService.isGeneralValidated(staffID, counterID, customerGender, customerName, customerPhone,
                discountRate);

        if (error != null) {
            return ResponseEntity.badRequest().body(error);
        }

        Customer customer = customerService.getCustomerByPhone(customerPhone);

        if (customer == null) {
            Customer newCustomer = new Customer();
            newCustomer.setID(0);
            newCustomer.setName(customerName);
            newCustomer.setGender(customerGender);
            newCustomer.setContactInfo(customerPhone);
            newCustomer.setPoint(0);
            if (!customerService.saveCustomer(newCustomer)) {
                return ResponseEntity.internalServerError().body("Error saving new customer");
            }

            customer = customerService.getCustomerByPhone(customerPhone); // get new customer info
        }

        int customerID = customer.getID();

        int orderID = orderService.generateID();

        totalPrice = totalPrice - (totalPrice * discountRate); // Discount range from 0 to 1

        totalPrice = totalPrice - pointApplied; // 1 point == 1

        int discountID = customerPromotionService.getCustomerPromotion(discountName).getID();

        Order newOrder = new Order();
        newOrder.setID(orderID);
        newOrder.setDate(Timestamp.now());
        newOrder.setStaffID(staffID);
        newOrder.setCounterID(counterID);
        newOrder.setCustomerID(customerID);
        newOrder.setTotalPrice(totalPrice);
        newOrder.setDiscountID(discountID);
        newOrder.setDiscountApplied(discountRate);
        newOrder.setPointApplied(pointApplied);
        try {
            orderService.saveOrder(newOrder);
            if (totalPrice >= ACCEPTABLE_TOTAL_PRICE) {
                int currentPoints = customer.getPoint();
                int additionalPoints = (int) (totalPrice / 100);
                customer.setPoint(currentPoints + additionalPoints);
                customerService.saveCustomer(customer);
            }

            List<OrderDTO> orderDTOs = new ArrayList<>();
            for (CartItem item : cart) {
                double productOriginalPrice = productService.getProductByID(item.getProduct().getID()).getPrice();

                OrderDTO orderDTO = new OrderDTO();
                orderDTO.setOrderID(newOrder.getID());
                orderDTO.setProductID(item.getProduct().getID());
                orderDTO.setAmount(item.getQuantity());
                orderDTO.setProductPrice(item.getPrice());
                orderDTO.setProductOriginalPrice(productOriginalPrice);
                orderDTOs.add(orderDTO);
                orderService.saveOrderDTO(orderDTO);
            }
            // Update new stock
            for (OrderDTO orderDTO : orderDTOs) {
                Product product = productService.getProductByID(orderDTO.getProductID());
                product.setStock(product.getStock() - orderDTO.getAmount());
                productService.saveProduct(product);
            }

            int currentPoints = customer.getPoint();
            customer.setPoint(currentPoints - pointApplied); // update new point
            customerService.saveCustomer(customer);
            return ResponseEntity.status(HttpStatus.SC_CREATED).body("Create order Successfully");
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
                String discountName = customerPromotionService.getCustomerPromotion(order.getDiscountID())
                        .getDiscountName();
                Map<String, Object> orderMap = new HashMap<>();
                orderMap.put("id", order.getID());
                orderMap.put("date", order.getDate());
                orderMap.put("staffID", order.getStaffID());
                orderMap.put("counterID", order.getCounterID());
                orderMap.put("totalPrice", order.getTotalPrice());
                orderMap.put("discountName", discountName);
                orderMap.put("discountApplied", order.getDiscountApplied());
                orderMap.put("pointApplied", order.getPointApplied());

                Customer customer = customerService.getCustomer(order.getCustomerID());
                String customerName = customer.getName();
                String customerPhone = customer.getContactInfo();
                orderMap.put("customerName", customerName);
                orderMap.put("customerPhone", customerPhone);

                User staff = userService.getUserByField(order.getStaffID(), "id", "user");
                String staffName = "N/A";
                if (staff != null) {
                    staffName = staff.getFullName();
                }
                orderMap.put("staffName", staffName);

                responseList.add(orderMap);
            }

            return ResponseEntity.ok(responseList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/v2/orders/order")
    public ResponseEntity<Map<String, Object>> getOrderV2(@RequestParam int id) {
        try {
            Order order = orderService.getOrder(id);

            if (order == null) {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
            }
            String discountName = customerPromotionService.getCustomerPromotion(order.getDiscountID())
                    .getDiscountName();
            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("id", order.getID());
            orderMap.put("date", order.getDate());
            orderMap.put("staffID", order.getStaffID());
            orderMap.put("counterID", order.getCounterID());
            orderMap.put("totalPrice", order.getTotalPrice());
            orderMap.put("discountName", discountName);
            orderMap.put("discountApplied", order.getDiscountApplied());
            orderMap.put("pointApplied", order.getPointApplied());

            Customer customer = customerService.getCustomer(order.getCustomerID());
            String customerName = customer.getName();
            String customerPhone = customer.getContactInfo();
            orderMap.put("customerName", customerName);
            orderMap.put("customerPhone", customerPhone);

            User staff = userService.getUserByField(order.getStaffID(), "id", "user");
            String staffName = "N/A";
            if (staff != null) {
                staffName = staff.getFullName();
            }
            orderMap.put("staffName", staffName);

            return ResponseEntity.ok(orderMap);
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
                orderDetailMap.put("Product", orderDetail.getAmount());
                orderDetailMap.put("productPrice", orderDetail.getProductPrice());
                orderDetailMap.put("productOriginalPrice", orderDetail.getProductOriginalPrice());

                responseList.add(orderDetailMap);
            }

            return ResponseEntity.ok(responseList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/v2/orders/search")
    public ResponseEntity<List<Map<String, Object>>> searchOrderListV2(
            @RequestParam String input,
            @RequestParam String filter) {
        try {
            List<Order> orderList = new ArrayList<>();
            orderList = orderService.searchOrderList(input, filter, orderService.getOrderList());

            if (orderList == null || orderList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);

            }

            List<Map<String, Object>> responseList = new ArrayList<>();
            for (Order order : orderList) {
                String discountName = customerPromotionService.getCustomerPromotion(order.getDiscountID())
                        .getDiscountName();
                Map<String, Object> orderMap = new HashMap<>();
                orderMap.put("id", order.getID());
                orderMap.put("date", order.getDate());
                orderMap.put("staffID", order.getStaffID());
                orderMap.put("counterID", order.getCounterID());
                orderMap.put("totalPrice", order.getTotalPrice());
                orderMap.put("discountName", discountName);
                orderMap.put("discountApplied", order.getDiscountApplied());

                Customer customer = customerService.getCustomer(order.getCustomerID());
                String customerName = customer.getName();
                String customerPhone = customer.getContactInfo();
                orderMap.put("customerName", customerName);
                orderMap.put("customerPhone", customerPhone);

                User staff = userService.getUserByField(order.getStaffID(), "id", "user");
                String staffName = "N/A";
                if (staff != null) {
                    staffName = staff.getFullName();
                }
                orderMap.put("staffName", staffName);

                responseList.add(orderMap);
            }

            return ResponseEntity.ok(responseList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/v2/orders/order/products/top")
    public ResponseEntity<List<Map<String, Object>>> getTopSellingProducts() {
        try {
            List<OrderDTO> orderDetails = orderService.getAllOrderDetails();

            Map<Integer, Integer> productSales = new HashMap<>();
            for (OrderDTO orderDetail : orderDetails) {
                int productId = orderDetail.getProductID();
                int amount = orderDetail.getAmount();
                productSales.put(productId, productSales.getOrDefault(productId, 0) + amount);
            }

            List<Map.Entry<Integer, Integer>> sortedSales = new ArrayList<>(productSales.entrySet());
            sortedSales.sort((a, b) -> b.getValue().compareTo(a.getValue()));

            List<Map<String, Object>> topProducts = new ArrayList<>();
            for (int i = 0; i < Math.min(10, sortedSales.size()); i++) {
                int productId = sortedSales.get(i).getKey();
                int totalAmount = sortedSales.get(i).getValue();
                Product product = productService.getProductByID(productId);

                if (product != null) {
                    Map<String, Object> productMap = new HashMap<>();
                    productMap.put("productName", product.getName());
                    productMap.put("totalAmount", totalAmount);
                    topProducts.add(productMap);
                } else {
                    // Log the missing product
                    System.err.println("Product not found for ID: " + productId);
                }
            }

            return ResponseEntity.ok(topProducts);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/v2/orders/categories/sales")
    public ResponseEntity<Map<String, Double>> getSalesDistributionByCategory() {
        try {
            List<OrderDTO> orderDetails = orderService.getAllOrderDetails();
            Map<Integer, String> productCategories = productService.getAllProductCategories();

            Map<String, Double> salesByCategory = new HashMap<>();

            for (OrderDTO orderDetail : orderDetails) {
                int productId = orderDetail.getProductID();
                String category = productCategories.getOrDefault(productId, "Unknown");

                double amount = orderDetail.getAmount();
                salesByCategory.put(category, salesByCategory.getOrDefault(category, 0.0) + amount);
            }

            return ResponseEntity.ok(salesByCategory);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
