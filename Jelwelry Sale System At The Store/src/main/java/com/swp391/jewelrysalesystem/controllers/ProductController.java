package com.swp391.jewelrysalesystem.controllers;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.swp391.jewelrysalesystem.models.Product;
import com.swp391.jewelrysalesystem.services.IProductService;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final IProductService productService;

    @Autowired
    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/product/add")
    public ResponseEntity<Product> addProduct(
        @RequestParam int ID,
        @RequestParam String name,
        @RequestParam double price,
        @RequestParam double refundPrice,
        @RequestParam String description,
        @RequestParam double goldWeight,
        @RequestParam double laborCost,
        @RequestParam double stoneCost,
        @RequestParam int stock,
        @RequestParam String category,
        @RequestParam String img,
        @RequestParam int promotionID) {

        try {
            Product newProduct = new Product(ID, img, name, price, refundPrice, description, goldWeight, laborCost, stoneCost, stock, promotionID, category, true);
            if (productService.saveProduct(newProduct)) {
                return ResponseEntity.status(HttpStatus.CREATED).build();
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/v2/products")
    public ResponseEntity<String> addProductV2(
        @RequestParam int ID,
        @RequestParam String name,
        @RequestParam double price,
        @RequestParam double refundPrice,
        @RequestParam String description,
        @RequestParam double goldWeight,
        @RequestParam double laborCost,
        @RequestParam double stoneCost,
        @RequestParam int stock,
        @RequestParam String category,
        @RequestParam String img,
        @RequestParam int promotionID) {
        
        try {
            if (productService.isNotNullProduct(ID)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Duplicated ID");
            }

            Product newProduct = new Product(ID, img, name, price, refundPrice, description, goldWeight, laborCost, stoneCost, stock, promotionID, category, true);
            if (productService.saveProduct(newProduct)) {
                return ResponseEntity.status(HttpStatus.CREATED).build();
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/product/{ID}/update-info")
    public ResponseEntity<Product> updateProductInfo(
        @PathVariable int ID,
        @RequestParam String name,
        @RequestParam double price,
        @RequestParam double refundPrice,
        @RequestParam String description,
        @RequestParam double goldWeight,
        @RequestParam double laborCost,
        @RequestParam double stoneCost,
        @RequestParam int stock,
        @RequestParam String category,
        @RequestParam String img,
        @RequestParam int promotionID) {

        try {
            Product existingProduct = productService.getProductByID(ID);
            if (existingProduct == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            existingProduct.setName(name);
            existingProduct.setImg(img);
            existingProduct.setPrice(price);
            existingProduct.setRefundPrice(refundPrice);
            existingProduct.setDescription(description);
            existingProduct.setGoldWeight(goldWeight);
            existingProduct.setLaborCost(laborCost);
            existingProduct.setStoneCost(stoneCost);
            existingProduct.setStock(stock);
            existingProduct.setCategory(category);
            existingProduct.setPromotionID(promotionID);

            if (productService.saveProduct(existingProduct)) {
                return ResponseEntity.ok(existingProduct);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/v2/products/{ID}")
    public ResponseEntity<Product> updateProductInfoV2(
        @PathVariable int ID,
        @RequestParam String name,
        @RequestParam double price,
        @RequestParam double refundPrice,
        @RequestParam String description,
        @RequestParam double goldWeight,
        @RequestParam double laborCost,
        @RequestParam double stoneCost,
        @RequestParam int stock,
        @RequestParam String category,
        @RequestParam String img,
        @RequestParam int promotionID) {

        try {
            Product existingProduct = productService.getProductByID(ID);
            if (existingProduct == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            existingProduct.setName(name);
            existingProduct.setImg(img);
            existingProduct.setPrice(price);
            existingProduct.setRefundPrice(refundPrice);
            existingProduct.setDescription(description);
            existingProduct.setGoldWeight(goldWeight);
            existingProduct.setLaborCost(laborCost);
            existingProduct.setStoneCost(stoneCost);
            existingProduct.setStock(stock);
            existingProduct.setCategory(category);
            existingProduct.setPromotionID(promotionID);

            if (productService.saveProduct(existingProduct)) {
                return ResponseEntity.ok(existingProduct);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/v2/products/{ID}")
    public ResponseEntity<String> deleteProduct(@PathVariable int ID){
        if (productService.isNotNullProduct(ID)) {
            return productService.deleteProduct(ID)?ResponseEntity.ok("Delete Successfully"):ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failing to delete product ID: " + ID);
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failing to delete product ID: " + ID);
    }

    @GetMapping("/product/list")
    public ResponseEntity<List<Product>> getProductList() {
        try {
            List<Product> productList = productService.getProductList();
            if (productList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(productList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/v2/products")
    public ResponseEntity<List<Product>> getProductListV2() {
        try {
            List<Product> productList = productService.getProductList();
            if (productList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(productList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/product/{ID}/info")
    public ResponseEntity<Product> getProduct(@PathVariable int ID) {
        try {
            Product product = productService.getProductByID(ID);
            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/v2/products/{ID}")
    public ResponseEntity<Product> getProductV2(@PathVariable int ID) {
        try {
            Product product = productService.getProductByID(ID);
            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/product/{ID}/change-status")
    public ResponseEntity<Product> changeProductStatus(@PathVariable int ID) {
        try {
            Product product = productService.changeProductStatus(ID);
            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    
    @PutMapping("/v2/products/{ID}/status")
    public ResponseEntity<Product> changeProductStatusV2(@PathVariable int ID) {
        try {
            Product product = productService.changeProductStatus(ID);
            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/product/list/search")
    public ResponseEntity<List<Product>> searchProduct(
        @RequestParam String input,
        @RequestParam String filter) {

        try {
            List<Product> productList = productService.searchProduct(input, filter, productService.getProductList());
            if (productList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(productList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/v2/products/search")
    public ResponseEntity<List<Product>> searchProductv2(
        @RequestParam String input,
        @RequestParam String filter) {

        try {
            List<Product> productList = productService.searchProduct(input, filter, productService.getProductList());
            if (productList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(productList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/product/list/sort")
    public ResponseEntity<List<Product>> sortProductList(
        @RequestParam String filter,
        @RequestParam String sortOrder) {

        try {
            List<Product> productList = productService.sortProduct(filter, sortOrder, productService.getProductList());
            if (productList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(productList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/v2/products/sort")
    public ResponseEntity<List<Product>> sortProductListV2(
        @RequestParam String filter,
        @RequestParam String sortOrder) {

        try {
            List<Product> productList = productService.sortProduct(filter, sortOrder, productService.getProductList());
            if (productList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(productList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/product/list/search/sort")
    public ResponseEntity<List<Product>> sortSearchedProduct(
        @RequestParam String input,
        @RequestParam String filter,
        @RequestParam String sortFilter,
        @RequestParam String sortOrder) {

        try {
            List<Product> productList = productService.searchProduct(input, filter, productService.getProductList());
            productList = productService.sortProduct(sortFilter, sortOrder, productList);
            if (productList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(productList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/v2/products/search/sort")
    public ResponseEntity<List<Product>> sortSearchedProductV2(
        @RequestParam String input,
        @RequestParam String filter,
        @RequestParam String sortFilter,
        @RequestParam String sortOrder) {

        try {
            List<Product> productList = productService.searchProduct(input, filter, productService.getProductList());
            productList = productService.sortProduct(sortFilter, sortOrder, productList);
            if (productList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(productList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
