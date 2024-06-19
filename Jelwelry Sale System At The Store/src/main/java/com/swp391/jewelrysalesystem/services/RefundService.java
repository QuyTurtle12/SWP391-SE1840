package com.swp391.jewelrysalesystem.services;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.swp391.jewelrysalesystem.models.ProductPurity;
import com.swp391.jewelrysalesystem.models.Refund;
import com.swp391.jewelrysalesystem.models.RefundDTO;

@Service
public class RefundService implements IRefundService {

    private IGenericService<Refund> genericService;
    private ICustomerService customerService;

    @Autowired
    public RefundService(IGenericService<Refund> genericService, ICustomerService customerService){
        this.genericService = genericService;
        this.customerService = customerService;
    }

    @Override
    public boolean saveRefundedOrder(Refund order) {
        return genericService.saveObject(order, "refund", order.getID());
    }

    @Override
    public boolean saveProduct(RefundDTO product) {
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            DocumentReference documentReference = dbFirestore.collection("refund").document(String.valueOf(product.getRefundID()))
            .collection("refundedProduct").document(String.valueOf(product.getProductID()));

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
            DocumentReference documentReference = dbFirestore.collection("refund").document(String.valueOf(refundID))
            .collection("refundedProduct").document(String.valueOf(product.getProductID()))
            .collection("productPurity").document(String.valueOf(product.getPurity()));

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
    public List<RefundDTO> getRefundedProductList(int refundID) {
        Firestore dbFirestore =FirestoreClient.getFirestore();
        CollectionReference collectionReference = dbFirestore.collection("refund").document(String.valueOf(refundID)).collection("refundedProduct");

        try {
            ApiFuture<QuerySnapshot> future = collectionReference.get();
            QuerySnapshot querySnapshot = future.get();
            return querySnapshot.toObjects(RefundDTO.class);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ProductPurity> getProductPurityList(int refundID, int productID) {
        Firestore dbFirestore =FirestoreClient.getFirestore();
        CollectionReference collectionReference = dbFirestore.collection("refund").document(String.valueOf(refundID))
        .collection("refundedProduct").document(String.valueOf(productID)).collection("productPurity");

        try {
            ApiFuture<QuerySnapshot> future = collectionReference.get();
            QuerySnapshot querySnapshot = future.get();
            return querySnapshot.toObjects(ProductPurity.class);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
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
            return customerService.isNotNullCustomer(ID) ? true : false;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
        
    }

    @Override
    public String isGeneralValidated(double totalPrice, int customerID) {

        if (totalPrice < 0) {
            return "Incorrect total price!";
        }

        if (!isNotNullCustomer(customerID)) {
            return "The customer ID " + customerID + " is not existing";
        }

        return null;
    }

    @Override
    public Refund getRefundedOrder(int ID) {
        try {
            return genericService.getByField(ID, "id", "refund", Refund.class);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
