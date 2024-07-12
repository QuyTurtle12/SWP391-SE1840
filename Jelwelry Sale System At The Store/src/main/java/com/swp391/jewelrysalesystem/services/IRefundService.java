package com.swp391.jewelrysalesystem.services;

import java.util.List;

import com.swp391.jewelrysalesystem.models.CartItem;
import com.swp391.jewelrysalesystem.models.Customer;
import com.swp391.jewelrysalesystem.models.ProductPurity;
import com.swp391.jewelrysalesystem.models.Refund;
import com.swp391.jewelrysalesystem.models.RefundDTO;

public interface IRefundService {
    boolean saveRefundedOrder(Refund order);

    boolean saveProduct(RefundDTO product);

    boolean saveProductPurity(ProductPurity product, int refundID);

    List<Refund> getRefundedOrderList();

    List<RefundDTO> getRefundedProductList(int refundID);

    List<ProductPurity> getProductPurityList(int refundID, int productID);

    Refund getRefundedOrder(int ID);

    boolean isNotNullRefundedOrder(int ID);

    boolean isNotNullCustomer(int ID);

    String isGeneralValidated(double totalPrice, Customer customer, int staffID);

    int generateID();

    double calculateRefundPrice(CartItem cartItem);
}
