package com.swp391.jewelrysalesystem.services;

import java.util.List;

import com.swp391.jewelrysalesystem.models.Category;

public interface ICategoryService {
    boolean saveCatogory(Category category);

    List<Category> getCategoryList();

    Category getCategory(int ID);

    boolean removeCategory(int ID);

    boolean isNotNullCategory(int ID);

}
