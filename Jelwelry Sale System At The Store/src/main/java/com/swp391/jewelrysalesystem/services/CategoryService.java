package com.swp391.jewelrysalesystem.services;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swp391.jewelrysalesystem.models.Category;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class CategoryService implements ICategoryService{

    private IGenericService<Category> genericService;

    @Autowired
    public CategoryService(IGenericService<Category> genericService){
        this.genericService = genericService;
    }

    @Override
    public boolean saveCatogory(Category category) {
        return genericService.saveObject(category, "category", category.getID());
    }

    @Override
    public List<Category> getCategoryList() {
        try {
            return genericService.getList("category", Category.class);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Category getCategory(int ID) {
        try {
            return genericService.getByField(ID, "id", "category", Category.class);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean removeCategory(int ID) {
        return genericService.deleteObject(ID, "category");
    }

    @Override
    public boolean isNotNullCategory(int ID) {
        if (getCategory(ID) == null) {
            return false;
        }

        return true;
    }

    @Override
    public Category getCategoryByName(String name) {
        try {
            return genericService.getByField(name, "name", "category", Category.class);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

}
