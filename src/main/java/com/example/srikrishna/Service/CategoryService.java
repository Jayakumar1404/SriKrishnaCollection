package com.example.srikrishna.Service;

import java.util.List;

import com.example.srikrishna.Entity.Category;

public interface CategoryService {


    List<Category> getAllCategories();


    Category getCategoryById(
            Long id
    );


    List<Category> searchCategories(
            String keyword
    );


    List<Category> filterCategories(
            String status
    );


    List<Category> searchAndFilterCategories(
            String keyword,
            String status
    );


    Category saveCategory(
            Category category
    );


    Category updateCategory(
            Category category
    );


    void activateCategory(
            Long id
    );


    void deactivateCategory(
            Long id
    );


    void deleteCategory(
            Long id
    );


    long getTotalCategories();


    long getActiveCategories();


    long getInactiveCategories();


    boolean categoryNameExists(
            String name,
            Long id
    );

}