package com.swp391.jewelrysalesystem.services;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.swp391.jewelrysalesystem.models.Product;

public interface IProductService {
    Product saveProduct(Product product);

    Product getProductByID(int ID) throws InterruptedException, ExecutionException;

    List<Product> getProductList() throws InterruptedException, ExecutionException;

    Product changeProductStatus(int ID) throws InterruptedException, ExecutionException;

    List<Product> searchProduct(String input, String filter, List<Product> productList);

    List<Product> sortProduct(String filter, String sortOrder, List<Product> productList);
}
