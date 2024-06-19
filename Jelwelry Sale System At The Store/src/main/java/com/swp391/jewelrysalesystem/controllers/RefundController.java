package com.swp391.jewelrysalesystem.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.Timestamp;
import com.swp391.jewelrysalesystem.models.CartItem;
import com.swp391.jewelrysalesystem.models.ProductPurity;
import com.swp391.jewelrysalesystem.models.Refund;
import com.swp391.jewelrysalesystem.models.RefundDTO;
import com.swp391.jewelrysalesystem.services.IProductService;
import com.swp391.jewelrysalesystem.services.IRefundService;
import java.util.ArrayList;
import java.util.List;
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
    @Autowired
    public RefundController(IRefundService refundService, IProductService productService){
        this.refundService = refundService;
        this.productService = productService;
    }

    @PostMapping("/refunds")
    public ResponseEntity<String> addRefundedOrder(
        @RequestParam int ID,
        @RequestParam double totalPrice,
        @RequestParam int customerID,
        @RequestBody List<CartItem> cart) throws InterruptedException, ExecutionException {
            
            if (refundService.isNotNullRefundedOrder(ID)) {
                return ResponseEntity.status(HttpStatus.SC_CONFLICT).body("The refund ID " + ID + " has been existed.");
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
                product.setProductName(productService.getProductByID(product.getProductID()).getName());
                product.setAmount(cartItem.getQuantity());
                refundedProducts.add(product);
                refundService.saveProduct(product);
            }
            return null;
    }
    
    @PostMapping("/refunds/itemPurity")
    public ResponseEntity<String> addProductPurity(
        @RequestParam int refundID,
        @RequestBody List<ProductPurity> products) throws InterruptedException, ExecutionException {

            if (!refundService.isNotNullRefundedOrder(refundID)) {
                return ResponseEntity.status(HttpStatus.SC_CONFLICT).body("The refund ID " + refundID + " is not existing.");
            }

            for (ProductPurity product : products) {
                refundService.saveProductPurity(product, refundID);
            }
            return null;
        }

    @GetMapping("/refunds")
    public ResponseEntity<List<Refund>> getRefundList() {
        List<Refund> refunds =  refundService.getRefundedOrderList();

        if (refunds.isEmpty() || refunds == null) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(refunds);
    }
    
    @GetMapping("/refunds/refund")
    public ResponseEntity<Refund> getRefund(@RequestParam int ID) {
        Refund refunds =  refundService.getRefundedOrder(ID);

        if (refunds == null) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(refunds);
    }

    @GetMapping("/refunds/refund/products")
    public ResponseEntity<List<RefundDTO>> getRefundedProductList(@RequestParam int refundID) {
        List<RefundDTO> refunds =  refundService.getRefundedProductList(refundID);

        if (refunds.isEmpty() || refunds == null) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(refunds);
    }

    @GetMapping("/refunds/refund/products/product")
    public ResponseEntity<List<ProductPurity>> getRefundedProductPurityList(
        @RequestParam int refundID,
        @RequestParam int productID
    ) {
        List<ProductPurity> refunds =  refundService.getProductPurityList(refundID, productID);

        if (refunds.isEmpty() || refunds == null) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(refunds);
    }
}
