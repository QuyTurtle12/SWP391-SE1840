package com.swp391.jewelrysalesystem.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.swp391.jewelrysalesystem.models.Product;

@Service
public class ProductService implements IProductService {

    @Override
    public boolean saveProduct(Product product) {
        try {
            Firestore dbFirestore = FirestoreClient.getFirestore();
            DocumentReference documentReference = dbFirestore.collection("product")
                    .document(String.valueOf(product.getID()));

            ApiFuture<com.google.cloud.firestore.WriteResult> future = documentReference.set(product);
                future.get();
                return true;
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Error saving product document: " + e.getMessage());
                e.printStackTrace();
                return false;
        }
    }

    @Override
    public Product getProductByID(int ID) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference product = dbFirestore.collection("product");

        // Create a query to get product info base on product ID
        Query query = product.whereEqualTo("id", ID);

        // Retrieve query result
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        List<Product> productList = querySnapshot.get().toObjects(Product.class);

        if (productList.isEmpty()) {
            System.out.println("User document with productID" + ID + "does not exist");
            return null;
        } else {
            return productList.get(0);
        }
    }

    @Override
    public List<Product> getProductList() throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference products = dbFirestore.collection("product");

        ApiFuture<QuerySnapshot> future = products.get();
        QuerySnapshot querySnapshot = future.get();

        List<Product> productList = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot) {
            productList.add(document.toObject(Product.class));
        }
        return productList;
    }

    @Override
    public Product changeProductStatus(int ID) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference productRefs = dbFirestore.collection("product").document(String.valueOf(ID));

        try {
            ApiFuture<DocumentSnapshot> future = productRefs.get();
            DocumentSnapshot document = future.get();

            if (document.exists()) {
                Product product = document.toObject(Product.class);
                if (product != null) {
                    product.setStatus(!product.getStatus());
                    productRefs.set(product);
                    return product;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error changing product status: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Product> searchProduct(String input, String filter, List<Product> productList) {
        List<Product> newProductList = new ArrayList<>();

        switch (filter) {
            case "ByName":
                for (Product product : productList) {
                    if (product.getName().toLowerCase().trim().contains(input.toLowerCase())) {
                        newProductList.add(product);
                    }
                }

                break;
            case "ByCategory":
                for (Product product: productList){
                    if (product.getCategory().toLowerCase().equals(input.toLowerCase())) {
                        newProductList.add(product);
                    }
                }

                break;
            case "ByStatus":
            for (Product product: productList){
                if (product.getStatus().toString().toLowerCase().equals(input.toLowerCase())) {
                        newProductList.add(product);
                    }
                }
                
                break;
            default:
                throw new IllegalArgumentException("Invalid filter: " + filter);
        }
        
        return newProductList;
    }

    @Override
    public List<Product> sortProduct(String filter, String sortOrder, List<Product> productList) {
        List<Product> sortedProductList = new ArrayList<>(productList);

        switch (filter) {
            case "ByAlphabet":
                if (sortOrder.equalsIgnoreCase("asc")) {
                    Collections.sort(sortedProductList, Comparator.comparing(Product::getName));
                } else if (sortOrder.equalsIgnoreCase("desc")) {
                    Collections.sort(sortedProductList, Comparator.comparing(Product::getName).reversed());
                }
                break;
            case "ByPrice":
                if (sortOrder.equalsIgnoreCase("asc")) {
                    Collections.sort(sortedProductList, Comparator.comparingDouble(Product::getPrice));
                } else if (sortOrder.equalsIgnoreCase("desc")) {
                    Collections.sort(sortedProductList, Comparator.comparingDouble(Product::getPrice).reversed());
                }
                break;
            default:
                break;
        }
        
        return sortedProductList;
    }   

    @Override
    public boolean deleteProduct(int ID) {
        Firestore dbdFirestore = FirestoreClient.getFirestore();
        DocumentReference productRef = dbdFirestore.collection("product").document(String.valueOf(ID));

        try {
            ApiFuture<WriteResult> future = productRef.delete();
            future.get();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isNotNullProduct(int ID) {
        try {
            if (getProductByID(ID) != null) {
                return true;
            } else {
                return false;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }
}
