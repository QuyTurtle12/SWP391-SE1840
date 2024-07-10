package com.swp391.jewelrysalesystem.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.Timestamp;
import com.swp391.jewelrysalesystem.models.CartItem;
import com.swp391.jewelrysalesystem.models.Customer;
import com.swp391.jewelrysalesystem.models.ProductPurity;
import com.swp391.jewelrysalesystem.models.Refund;
import com.swp391.jewelrysalesystem.models.RefundDTO;
import com.swp391.jewelrysalesystem.services.ICustomerService;
import com.swp391.jewelrysalesystem.services.IProductService;
import com.swp391.jewelrysalesystem.services.IRefundService;
import com.swp391.jewelrysalesystem.services.IUserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api")
public class RefundController {
    private IRefundService refundService;
    private IProductService productService;
    private ICustomerService customerService;
    private IUserService userService;

    @Autowired
    public RefundController(IRefundService refundService, IProductService productService,
            ICustomerService customerService, IUserService userService) {
        this.refundService = refundService;
        this.productService = productService;
        this.customerService = customerService;
        this.userService = userService;
    }

    @PostMapping("/refunds")
    public ResponseEntity<String> addRefundedOrder(
            @RequestParam double totalPrice,
            @RequestParam String customerPhone,
            @RequestParam int staffID,
            @RequestBody List<CartItem> cart) throws InterruptedException, ExecutionException {

        Customer customer = customerService.getCustomerByPhone(customerPhone);

        String error = refundService.isGeneralValidated(totalPrice, customer, staffID);
        if (error != null) {
            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(error);
        }

        int customerID = customer.getID();

        int refundID = refundService.generateID();

        Refund refund = new Refund();
        refund.setID(refundID);
        refund.setTotalPrice(totalPrice);
        refund.setCustomerID(customerID);
        refund.setDate(Timestamp.now());
        refund.setStaffID(staffID);

        refundService.saveRefundedOrder(refund);
        List<RefundDTO> refundedProducts = new ArrayList<>();
        for (CartItem cartItem : cart) {
            RefundDTO product = new RefundDTO();
            product.setRefundID(refundID);
            product.setProductID(cartItem.getProduct().getID());
            product.setProductName(productService.getProductByID(product.getProductID()).getName());
            product.setAmount(cartItem.getQuantity());
            refundedProducts.add(product);
            refundService.saveProduct(product);
        }
        return ResponseEntity.ok().body(String.valueOf(refundID));
    }

    @PostMapping("/refunds/itemPurity")
    public ResponseEntity<String> addProductPurity(
            @RequestParam int refundID,
            @RequestBody List<ProductPurity> products) throws InterruptedException, ExecutionException {

        if (!refundService.isNotNullRefundedOrder(refundID)) {
            return ResponseEntity.status(HttpStatus.SC_CONFLICT)
                    .body("The refund ID " + refundID + " is not existing.");
        }

        for (ProductPurity product : products) {
            refundService.saveProductPurity(product, refundID);
        }
        return null;
    }

    @GetMapping("/refunds")
    public ResponseEntity<List<Map<String, Object>>> getRefundList() throws InterruptedException, ExecutionException {
        List<Refund> refunds = refundService.getRefundedOrderList();

        if (refunds.isEmpty() || refunds == null) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
        }

        List<Map<String, Object>> response = new ArrayList<>();
        for (Refund refund : refunds) {
            String customerName = customerService.getCustomer(refund.getID()).getName();
            String staffName = userService.getUserByField(refund.getStaffID(), "id", "user").getFullName();

            Map<String, Object> map = new HashMap<>();
            map.put("ID", refund.getID());
            map.put("date", refund.getDate());
            map.put("totalPrice", refund.getTotalPrice());
            map.put("customerName", customerName);
            map.put("staffID", refund.getStaffID());
            map.put("staffName", staffName);

            response.add(map);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/refunds/refund")
    public ResponseEntity<Map<String, Object>> getRefund(@RequestParam int ID) throws InterruptedException, ExecutionException {
        Refund refund = refundService.getRefundedOrder(ID);

        if (refund == null) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
        }

            String customerName = customerService.getCustomer(refund.getID()).getName();
            String staffName = userService.getUserByField(refund.getStaffID(), "id", "user").getFullName();

            Map<String, Object> map = new HashMap<>();
            map.put("ID", refund.getID());
            map.put("date", refund.getDate());
            map.put("totalPrice", refund.getTotalPrice());
            map.put("customerName", customerName);
            map.put("staffID", refund.getStaffID());
            map.put("staffName", staffName);


        return ResponseEntity.ok(map);
    }

    @GetMapping("/refunds/refund/products")
    public ResponseEntity<List<RefundDTO>> getRefundedProductList(@RequestParam int refundID) {
        List<RefundDTO> refunds = refundService.getRefundedProductList(refundID);

        if (refunds.isEmpty() || refunds == null) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(refunds);
    }

    @GetMapping("/refunds/refund/products/product")
    public ResponseEntity<List<ProductPurity>> getRefundedProductPurityList(
            @RequestParam int refundID,
            @RequestParam int productID) {
        List<ProductPurity> refunds = refundService.getProductPurityList(refundID, productID);

        if (refunds.isEmpty() || refunds == null) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(refunds);
    }
}
