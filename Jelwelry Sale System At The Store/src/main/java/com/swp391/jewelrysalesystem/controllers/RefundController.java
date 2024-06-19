package com.swp391.jewelrysalesystem.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.Timestamp;
import com.swp391.jewelrysalesystem.models.CartItem;
import com.swp391.jewelrysalesystem.models.ProductPurity;
import com.swp391.jewelrysalesystem.models.Refund;
import com.swp391.jewelrysalesystem.models.RefundDTO;
import com.swp391.jewelrysalesystem.services.IRefundService;
import com.swp391.jewelrysalesystem.services.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api")
public class RefundController {
    private IRefundService refundService;

    @Autowired
    public RefundController(IRefundService refundService){
        this.refundService = refundService;
    }

    @PostMapping("/refunds")
    public ResponseEntity<String> addRefundedOrder(
        @RequestParam int ID,
        @RequestParam double totalPrice,
        @RequestParam int customerID,
        @RequestParam int refundedProductQuantity,
        @RequestParam int refundedProductQuantityPerPurity,
        @RequestBody List<CartItem> cart) throws InterruptedException, ExecutionException {
            
            if (refundService.isNotNullRefundedOrder(ID)) {
                return ResponseEntity.status(HttpStatus.SC_CONFLICT).body("The ID " + ID + " has been existed.");
            }

            String error = refundService.isGeneralValidated(totalPrice, customerID);
            if (error != null) {
                return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(error);
            }

            Refund refund = new Refund();
            refund.setID(customerID);
            refund.setTotalPrice(totalPrice);
            refund.setCustomerID(customerID);
            refund.setDate(Timestamp.now());

            refundService.saveRefundedOrder(refund);
            List<RefundDTO> refundedProducts = new ArrayList<>(); 
            for (CartItem cartItem : cart) {
                RefundDTO product = new RefundDTO();
                product.setRefundID(ID);
                product.setProductID(cartItem.getProduct().getID());
                product.setProductName(new ProductService().getProductByID(product.getProductID()).getName());
                product.setAmount(refundedProductQuantity);
                refundedProducts.add(product);
                refundService.saveProduct(product);
            }
            return null;
    }
    

}
