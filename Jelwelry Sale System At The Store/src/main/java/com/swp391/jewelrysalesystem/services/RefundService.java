package com.swp391.jewelrysalesystem.services;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.swp391.jewelrysalesystem.models.ProductPurity;
import com.swp391.jewelrysalesystem.models.Refund;
import com.swp391.jewelrysalesystem.models.RefundDTO;

@Service
public class RefundService implements IRefundService {

    private IGenericService<Refund> genericService;

    @Autowired
    public RefundService(IGenericService<Refund> genericService){
        this.genericService = genericService;
    }

    @Override
    public boolean saveRefundedOrder(Refund order) {
        return genericService.saveObject(order, "Refund", order.getID());
    }

    @Override
    public boolean saveProduct(RefundDTO product) {
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            DocumentReference documentReference = dbFirestore.collection("Refund").document(String.valueOf(product.getRefundID()))
            .collection("RefundedProduct").document(String.valueOf(product.getProductID()));

            ApiFuture<WriteResult> future = documentReference.set(product);
            future.get();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean saveProductPurity(ProductPurity product, int refundID) {
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            DocumentReference documentReference = dbFirestore.collection("Refund").document(String.valueOf(refundID))
            .collection("RefundedProduct").document(String.valueOf(product.getProductID()))
            .collection("ProductPurity").document(String.valueOf(product.getPurity()));

            ApiFuture<WriteResult> future = documentReference.set(product);
            future.get();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Refund> getRefundedOrderList() {
        try {
            return genericService.getList("refund", Refund.class);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<RefundDTO> getRefundedProductList(int ID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRefundedProductList'");
    }

    @Override
    public List<ProductPurity> getProductPurityList(int ID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProductPurityList'");
    }

    @Override
    public boolean isNotNullRefundedOrder(int ID) {
        try {
            if (genericService.getByField(ID, "id", "refund", Refund.class) != null) {
                return true;
            }

            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isNotNullCustomer(int ID) {
        try {
            return new CustomerService().isNotNullCustomer(ID) ? true : false;
        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        
    }

    @Override
    public String isGeneralValidated(double totalPrice, int customerID) {

        if (totalPrice < 0) {
            return "Incorrect total price!";
        }

        if (isNotNullCustomer(customerID)) {
            return "The customer ID " + customerID + " is not existing";
        }

        return null;
    }
}
