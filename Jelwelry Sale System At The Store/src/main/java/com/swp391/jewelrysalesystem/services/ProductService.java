package com.swp391.jewelrysalesystem.services;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.swp391.jewelrysalesystem.models.Product;

@Service
public class ProductService implements IProductService {
    private IGenericService<Product> genericService;
    private ICategoryService categoryService;
    private IPromotionService promotionService;

    @Autowired
    public ProductService(IGenericService<Product> genericService, ICategoryService categoryService, IPromotionService promotionService) {
        this.genericService = genericService;
        this.categoryService = categoryService;
        this.promotionService = promotionService;
    }

    @Override
    public boolean saveProduct(Product product) {
        return genericService.saveObject(product, "product", product.getID());
    }

    @Override
    public Product getProductByID(int ID) throws InterruptedException, ExecutionException {
        return genericService.getByField(ID, "id", "product", Product.class);
    }

    @Override
    public List<Product> getProductList() throws InterruptedException, ExecutionException {
        return genericService.getList("product", Product.class);
    }

    @Override
    public boolean changeProductStatus(int ID) throws InterruptedException, ExecutionException {
        return genericService.changeStatus(ID, "product", Product.class);
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
                for (Product product : productList) {
                    if (product.getCategoryID() == Integer.parseInt(input)) {
                        newProductList.add(product);
                    }
                }

                break;
            case "ByStatus":
                for (Product product : productList) {
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
        return genericService.deleteObject(ID, "product");
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

    @Override
    public String isGeneralValidated(String name, int categoryID, double price, double refundPrice, double goldWeight,
            double laborCost, double stoneCost, int stock, String img, int promotionID) {

        String error = null;
        if (name.isBlank() || name.equals(null)) {
            return "Name cannot not be empty";
        }

        if (!categoryService.isNotNullCategory(categoryID)) {
            return "Incorrect category!";
        }

        if (price < 0) {
            return "Price cannot be negative";
        }

        if (refundPrice < 0) {
            return "Refund price cannot be negative";
        }

        if (goldWeight < 0) {
            return "Gold weight cannot be negative";
        }

        if (laborCost < 0) {
            return "Labor cost cannot be negative";
        }

        if (stoneCost < 0) {
            return "Stone cost cannot be negative";
        }

        if (stock < 0) {
            return "Stock cannot be negative";
        }

        if (!promotionService.isNotNullPromotion(promotionID)) {
            return "Invalid Promotion!";
        }

        if (!isValidImageUrl(img)) {
            return "Invalid image URL";
        }
        return error;
    }

    private boolean isValidImageUrl(String url) {
        // Regular expression to check URL format
        String urlPattern = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";
        if (!Pattern.compile(urlPattern).matcher(url).matches()) {
            return false;
        }

        try {
            URL imageUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
            connection.setRequestMethod("HEAD");
            connection.connect();
            String contentType = connection.getContentType();
            return contentType.startsWith("image/");
        } catch (Exception e) {
            return false;
        }
    }
}
