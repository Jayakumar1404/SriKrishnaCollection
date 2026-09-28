package com.example.srikrishna.ServiceImpl;

import com.example.srikrishna.Service.*;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.srikrishna.Entity.Category;
import com.example.srikrishna.Repository.CategoryRepository;

@Service
public class CategoryServiceImpl
        implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(
            CategoryRepository categoryRepository) {

        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {

        return categoryRepository
                .findAllByOrderByCreatedAtDesc();
    }

    @Override
    public Category getCategoryById(Long id) {

        return categoryRepository
                .findById(id)
                .orElse(null);
    }

    @Override
    public List<Category> searchCategories(
            String keyword) {

        if (keyword == null ||
                keyword.trim().isEmpty()) {

            return getAllCategories();
        }

        return categoryRepository
                .findByNameContainingIgnoreCaseOrderByCreatedAtDesc(
                        keyword.trim());
    }

    @Override
    public List<Category> filterCategories(
            String status) {

        if (status == null ||
                status.equalsIgnoreCase("ALL")) {

            return getAllCategories();
        }

        if (status.equalsIgnoreCase("ACTIVE")) {

            return categoryRepository
                    .findByStatusOrderByCreatedAtDesc(
                            true);
        }

        if (status.equalsIgnoreCase("INACTIVE")) {

            return categoryRepository
                    .findByStatusOrderByCreatedAtDesc(
                            false);
        }

        return getAllCategories();
    }

    @Override
    public List<Category> searchAndFilterCategories(
            String keyword,
            String status) {

        boolean hasKeyword = keyword != null &&
                !keyword.trim().isEmpty();

        boolean allStatus = status == null ||
                status.trim().isEmpty() ||
                status.equalsIgnoreCase("ALL");

        if (!hasKeyword && allStatus) {

            return getAllCategories();
        }

        if (!hasKeyword) {

            return filterCategories(status);
        }

        if (allStatus) {

            return searchCategories(keyword);
        }

        Boolean active = status.equalsIgnoreCase("ACTIVE");

        return categoryRepository
                .findByNameContainingIgnoreCaseAndStatusOrderByCreatedAtDesc(
                        keyword.trim(),
                        active);
    }

    @Override
    public Category saveCategory(
            Category category) {

        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(
            Category category) {

        return categoryRepository.save(category);
    }

    @Override
    public void activateCategory(Long id) {

        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Category not found"));

        category.setStatus(true);

        categoryRepository.save(category);
    }

    @Override
    public void deactivateCategory(Long id) {

        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Category not found"));

        category.setStatus(false);

        categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {

        if (!categoryRepository.existsById(id)) {

            throw new RuntimeException(
                    "Category not found");
        }

        categoryRepository.deleteById(id);
    }

    @Override
    public long getTotalCategories() {

        return categoryRepository.count();
    }

    @Override
    public long getActiveCategories() {

        return categoryRepository.countByStatus(true);
    }

    @Override
    public long getInactiveCategories() {

        return categoryRepository.countByStatus(false);
    }

    @Override
    public boolean categoryNameExists(
            String name,
            Long id) {

        if (name == null ||
                name.trim().isEmpty()) {

            return false;
        }

        Category existing = categoryRepository
                .findByNameIgnoreCase(
                        name.trim())
                .orElse(null);

        if (existing == null) {

            return false;
        }

        if (id == null) {

            return true;
        }

        return !existing
                .getId()
                .equals(id);
    }

}