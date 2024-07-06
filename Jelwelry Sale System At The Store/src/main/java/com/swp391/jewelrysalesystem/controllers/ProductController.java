package com.swp391.jewelrysalesystem.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.swp391.jewelrysalesystem.models.Category;
import com.swp391.jewelrysalesystem.models.Product;
import com.swp391.jewelrysalesystem.models.Promotion;
import com.swp391.jewelrysalesystem.services.ICategoryService;
import com.swp391.jewelrysalesystem.services.IProductService;
import com.swp391.jewelrysalesystem.services.IPromotionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api")
public class ProductController {

    private IProductService productService;
    private ICategoryService categoryService;
    private IPromotionService promotionService;

    @Autowired
    public ProductController(IProductService productService, ICategoryService categoryService,
            IPromotionService promotionService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.promotionService = promotionService;
    }

    @PostMapping("/v2/products")
    public ResponseEntity<String> addProductV2(
            @RequestParam String name,
            @RequestParam double price,
            @RequestParam double refundPrice,
            @RequestParam String description,
            @RequestParam double goldWeight,
            @RequestParam double laborCost,
            @RequestParam double stoneCost,
            @RequestParam int stock,
            @RequestParam String categoryName,
            @RequestParam String img,
            @RequestParam int promotionID) {

        try {

            Category category = categoryService.getCategoryByName(categoryName);
            if (category == null) {
                return ResponseEntity.badRequest().body("This category is not exist");
            }

            int categoryID = category.getID();

            String error = productService.isGeneralValidated(name, price, refundPrice, goldWeight, laborCost, stoneCost,
                    stock, img, promotionID);
            if (error != null) {
                return ResponseEntity.badRequest().body(error);
            }

            int ID = productService.generateID();

            Promotion promotion = promotionService.getPromotion(promotionID);
            double discountPrice = price;
            if (promotion != null) {
                double discountRate = promotion.getDiscountRate();
                discountPrice = price - (price * discountRate);
            }

            Product newProduct = new Product(ID, img, name, price, refundPrice, discountPrice, description, goldWeight, laborCost,
                    stoneCost, stock, promotionID, categoryID, true);
            if (productService.saveProduct(newProduct)) {
                return ResponseEntity.status(HttpStatus.CREATED).build();
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/v2/products/{ID}")
    public ResponseEntity<String> updateProductInfoV2(
            @PathVariable int ID,
            @RequestParam String name,
            @RequestParam double price,
            @RequestParam double refundPrice,
            @RequestParam String description,
            @RequestParam double goldWeight,
            @RequestParam double laborCost,
            @RequestParam double stoneCost,
            @RequestParam int stock,
            @RequestParam String categoryName,
            @RequestParam String img,
            @RequestParam int promotionID) {

        try {

            if (!productService.isNotNullProduct(ID)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product ID " + ID + " is not existed!");
            }

            Category category = categoryService.getCategoryByName(categoryName);
            if (category == null) {
                return ResponseEntity.badRequest().body("This category is not exist");
            }

            int categoryID = category.getID();

            String error = productService.isGeneralValidated(name, price, refundPrice, goldWeight, laborCost, stoneCost,
                    stock, img, promotionID);
            if (error != null) {
                return ResponseEntity.badRequest().body(error);
            }

            Promotion promotion = promotionService.getPromotion(promotionID);
            double discountPrice = price;
            if (promotion != null) {
                double discountRate = promotion.getDiscountRate();
                discountPrice = price - (price * discountRate);
            }

            Product existingProduct = productService.getProductByID(ID);

            existingProduct.setName(name);
            existingProduct.setImg(img);
            existingProduct.setPrice(price);
            existingProduct.setRefundPrice(refundPrice);
            existingProduct.setDiscountPrice(discountPrice);
            existingProduct.setDescription(description);
            existingProduct.setGoldWeight(goldWeight);
            existingProduct.setLaborCost(laborCost);
            existingProduct.setStoneCost(stoneCost);
            existingProduct.setStock(stock);
            existingProduct.setCategoryID(categoryID);
            existingProduct.setPromotionID(promotionID);

            if (productService.saveProduct(existingProduct)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/v2/products/{ID}")
    public ResponseEntity<String> deleteProduct(@PathVariable int ID) {
        if (productService.isNotNullProduct(ID)) {
            return productService.deleteProduct(ID) ? ResponseEntity.ok("Delete Successfully")
                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Failing to delete product ID: " + ID);
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failing to delete product ID: " + ID);
    }

    @GetMapping("/v2/products")
    public ResponseEntity<List<Map<String, Object>>> getProductListV2() {
        try {
            List<Product> productList = productService.getProductList();
            if (productList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            List<Map<String, Object>> productMaps = new ArrayList<>();
            for (Product product : productList) {
                String categoryName = categoryService.getCategory(product.getCategoryID()).getName();

                Map<String, Object> productMap = new HashMap<>();
                productMap.put("id", product.getID());
                productMap.put("img", product.getImg());
                productMap.put("name", product.getName());
                productMap.put("price", product.getPrice());
                productMap.put("refundPrice", product.getRefundPrice());
                productMap.put("discountPrice", product.getDiscountPrice());
                productMap.put("description", product.getDescription());
                productMap.put("goldWeight", product.getGoldWeight());
                productMap.put("laborCost", product.getLaborCost());
                productMap.put("stoneCost", product.getStoneCost());
                productMap.put("stock", product.getStock());
                productMap.put("promotionID", product.getPromotionID());
                productMap.put("categoryName", categoryName);
                productMap.put("status", product.getStatus());
                productMaps.add(productMap);
            }

            return ResponseEntity.ok(productMaps);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/v2/products/{ID}")
    public ResponseEntity<Map<String, Object>> getProductV2(@PathVariable int ID) {
        try {
            Product product = productService.getProductByID(ID);
            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            String categoryName = categoryService.getCategory(product.getCategoryID()).getName();

            Map<String, Object> productMap = new HashMap<>();
            productMap.put("id", product.getID());
            productMap.put("img", product.getImg());
            productMap.put("name", product.getName());
            productMap.put("price", product.getPrice());
            productMap.put("refundPrice", product.getRefundPrice());
            productMap.put("discountPrice", product.getDiscountPrice());
            productMap.put("description", product.getDescription());
            productMap.put("goldWeight", product.getGoldWeight());
            productMap.put("laborCost", product.getLaborCost());
            productMap.put("stoneCost", product.getStoneCost());
            productMap.put("stock", product.getStock());
            productMap.put("promotionID", product.getPromotionID());
            productMap.put("categoryName", categoryName);
            productMap.put("status", product.getStatus());
            return ResponseEntity.ok(productMap);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/v2/products/{ID}/status")
    public ResponseEntity<String> changeProductStatusV2(@PathVariable int ID) {
        try {
            boolean product = productService.changeProductStatus(ID);
            if (product == false) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product with ID " + ID + " is not existing");
            }
            return ResponseEntity.ok().body("Change status Successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/v2/products/sort")
    public ResponseEntity<List<Map<String, Object>>> sortProductListV2(
            @RequestParam String filter,
            @RequestParam String sortOrder) {

        try {
            List<Product> productList = productService.sortProduct(filter, sortOrder, productService.getProductList());
            if (productList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            List<Map<String, Object>> productMaps = new ArrayList<>();
            for (Product product : productList) {
                String categoryName = categoryService.getCategory(product.getCategoryID()).getName();

                Map<String, Object> productMap = new HashMap<>();
                productMap.put("id", product.getID());
                productMap.put("img", product.getImg());
                productMap.put("name", product.getName());
                productMap.put("price", product.getPrice());
                productMap.put("refundPrice", product.getRefundPrice());
                productMap.put("discountPrice", product.getDiscountPrice());
                productMap.put("description", product.getDescription());
                productMap.put("goldWeight", product.getGoldWeight());
                productMap.put("laborCost", product.getLaborCost());
                productMap.put("stoneCost", product.getStoneCost());
                productMap.put("stock", product.getStock());
                productMap.put("promotionID", product.getPromotionID());
                productMap.put("categoryName", categoryName);
                productMap.put("status", product.getStatus());
                productMaps.add(productMap);
            }

            return ResponseEntity.ok(productMaps);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/v2/products/search")
    public ResponseEntity<List<Map<String, Object>>> searchProductv2(
            @RequestParam String input,
            @RequestParam String filter) {

        try {
            List<Product> productList = productService.searchProduct(input, filter, productService.getProductList());
            if (productList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            List<Map<String, Object>> productMaps = new ArrayList<>();
            for (Product product : productList) {
                String categoryName = categoryService.getCategory(product.getCategoryID()).getName();

                Map<String, Object> productMap = new HashMap<>();
                productMap.put("id", product.getID());
                productMap.put("img", product.getImg());
                productMap.put("name", product.getName());
                productMap.put("price", product.getPrice());
                productMap.put("refundPrice", product.getRefundPrice());
                productMap.put("discountPrice", product.getDiscountPrice());
                productMap.put("description", product.getDescription());
                productMap.put("goldWeight", product.getGoldWeight());
                productMap.put("laborCost", product.getLaborCost());
                productMap.put("stoneCost", product.getStoneCost());
                productMap.put("stock", product.getStock());
                productMap.put("promotionID", product.getPromotionID());
                productMap.put("categoryName", categoryName);
                productMap.put("status", product.getStatus());
                productMaps.add(productMap);
            }

            return ResponseEntity.ok(productMaps);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/v2/products/search/sort")
    public ResponseEntity<List<Map<String, Object>>> sortSearchedProductV2(
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

            List<Map<String, Object>> productMaps = new ArrayList<>();
            for (Product product : productList) {
                String categoryName = categoryService.getCategory(product.getCategoryID()).getName();

                Map<String, Object> productMap = new HashMap<>();
                productMap.put("id", product.getID());
                productMap.put("img", product.getImg());
                productMap.put("name", product.getName());
                productMap.put("price", product.getPrice());
                productMap.put("refundPrice", product.getRefundPrice());
                productMap.put("discountPrice", product.getDiscountPrice());
                productMap.put("description", product.getDescription());
                productMap.put("goldWeight", product.getGoldWeight());
                productMap.put("laborCost", product.getLaborCost());
                productMap.put("stoneCost", product.getStoneCost());
                productMap.put("stock", product.getStock());
                productMap.put("promotionID", product.getPromotionID());
                productMap.put("categoryName", categoryName);
                productMap.put("status", product.getStatus());
                productMaps.add(productMap);
            }

            return ResponseEntity.ok(productMaps);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/v2/products/disable")
    public ResponseEntity<String> disablePromotionID(@RequestParam int promotionID) {
        if (!promotionService.isNotNullPromotion(promotionID)) {
            return ResponseEntity.badRequest().body("Promotion ID " + promotionID + " is not exist");
        }

        if (productService.disableProductPromotionID(promotionID)) {
            return ResponseEntity.ok().body("Disabled promotion of products which have promotion ID " + promotionID + " successfully");
        } else {
            return ResponseEntity.internalServerError().body("Error disabling promotion ID " + promotionID);
        }
    }
    
}
