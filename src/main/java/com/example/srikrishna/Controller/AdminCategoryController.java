package com.example.srikrishna.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.srikrishna.Entity.Category;
import com.example.srikrishna.Service.CategoryService;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;
    private final Cloudinary cloudinary;

    public AdminCategoryController(
            CategoryService categoryService,
            Cloudinary cloudinary) {

        this.categoryService = categoryService;
        this.cloudinary = cloudinary;
    }


    // =====================================================
    // CATEGORY LIST
    // =====================================================

    @GetMapping
    public String categories(
            @RequestParam(
                    value = "keyword",
                    required = false
            )
            String keyword,

            @RequestParam(
                    value = "status",
                    required = false,
                    defaultValue = "ALL"
            )
            String status,

            Model model) {

        List<Category> categories =
                categoryService.searchAndFilterCategories(
                        keyword,
                        status
                );

        model.addAttribute(
                "categories",
                categories
        );

        model.addAttribute(
                "keyword",
                keyword
        );

        model.addAttribute(
                "selectedStatus",
                status
        );

        model.addAttribute(
                "totalCategories",
                categoryService.getTotalCategories()
        );

        model.addAttribute(
                "activeCategories",
                categoryService.getActiveCategories()
        );

        model.addAttribute(
                "inactiveCategories",
                categoryService.getInactiveCategories()
        );

        // Important for Add/Edit modal/form
        if (!model.containsAttribute("category")) {
            model.addAttribute(
                    "category",
                    new Category()
            );
        }

        return "admin/categories";
    }


    // =====================================================
    // ADD CATEGORY PAGE
    // =====================================================

    @GetMapping("/new")
    public String newCategory(Model model) {

        model.addAttribute(
                "category",
                new Category()
        );

        model.addAttribute(
                "pageTitle",
                "Add Category"
        );

        return "admin/category-form";
    }


    // =====================================================
    // SAVE / UPDATE CATEGORY
    // =====================================================
    //
    // SAME URL handles both:
    //
    // NEW:
    // POST /admin/categories/save
    //
    // UPDATE:
    // POST /admin/categories/save
    //
    // =====================================================

    @PostMapping("/save")
    public String saveCategory(

            Category category,

            @RequestParam(
                    value = "imageFile",
                    required = false
            )
            MultipartFile imageFile,

            RedirectAttributes redirectAttributes) {

        try {

            // =================================================
            // DUPLICATE CATEGORY NAME
            // =================================================

            if (categoryService.categoryNameExists(
                    category.getName(),
                    category.getId())) {

                redirectAttributes.addFlashAttribute(
                        "error",
                        "Category name already exists."
                );

                if (category.getId() == null) {

                    return "redirect:/admin/categories/new";
                }

                return "redirect:/admin/categories/edit/"
                        + category.getId();
            }


            // =================================================
            // DEFAULT STATUS
            // =================================================

            if (category.getStatus() == null) {

                category.setStatus(true);
            }


            // =================================================
            // CLOUDINARY IMAGE UPLOAD
            // =================================================

            if (
                imageFile != null &&
                !imageFile.isEmpty()
            ) {

                Map uploadResult =
                        cloudinary.uploader().upload(
                                imageFile.getBytes(),

                                ObjectUtils.asMap(
                                        "folder",
                                        "srikrishna/categories"
                                )
                        );


                String imageUrl =
                        uploadResult
                                .get("secure_url")
                                .toString();


                // Store Cloudinary URL
                // directly in category.image

                category.setImage(imageUrl);
            }


            // =================================================
            // ADD CATEGORY
            // =================================================

            if (category.getId() == null) {

                categoryService.saveCategory(
                        category
                );

                redirectAttributes.addFlashAttribute(
                        "success",
                        "Category added successfully."
                );

            }


            // =================================================
            // UPDATE CATEGORY
            // =================================================

            else {

                Category existingCategory =
                        categoryService.getCategoryById(
                                category.getId()
                        );


                if (existingCategory == null) {

                    redirectAttributes.addFlashAttribute(
                            "error",
                            "Category not found."
                    );

                    return "redirect:/admin/categories";
                }


                // If user didn't select a new image,
                // keep the existing Cloudinary URL

                if (
                    imageFile == null ||
                    imageFile.isEmpty()
                ) {

                    category.setImage(
                            existingCategory.getImage()
                    );
                }


                categoryService.updateCategory(
                        category
                );


                redirectAttributes.addFlashAttribute(
                        "success",
                        "Category updated successfully."
                );
            }


        } catch (IOException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Image upload failed: "
                            + e.getMessage()
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Unable to save category: "
                            + e.getMessage()
            );
        }


        return "redirect:/admin/categories";
    }


    // =====================================================
    // EDIT CATEGORY
    // =====================================================

    @GetMapping("/edit/{id}")
    public String editCategory(

            @PathVariable Long id,

            Model model,

            RedirectAttributes redirectAttributes) {

        Category category =
                categoryService.getCategoryById(id);


        if (category == null) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Category not found."
            );

            return "redirect:/admin/categories";
        }


        model.addAttribute(
                "category",
                category
        );

        model.addAttribute(
                "pageTitle",
                "Edit Category"
        );

        return "admin/category-form";
    }


    // =====================================================
    // CATEGORY DETAILS
    // =====================================================

    @GetMapping("/{id}")
    public String categoryDetails(

            @PathVariable Long id,

            Model model,

            RedirectAttributes redirectAttributes) {

        Category category =
                categoryService.getCategoryById(id);


        if (category == null) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Category not found."
            );

            return "redirect:/admin/categories";
        }


        model.addAttribute(
                "category",
                category
        );

        return "admin/category-details";
    }


    // =====================================================
    // ACTIVATE
    // =====================================================

    @PostMapping("/{id}/activate")
    public String activateCategory(

            @PathVariable Long id,

            RedirectAttributes redirectAttributes) {

        try {

            categoryService.activateCategory(id);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Category activated successfully."
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/admin/categories";
    }


    // =====================================================
    // DEACTIVATE
    // =====================================================

    @PostMapping("/{id}/deactivate")
    public String deactivateCategory(

            @PathVariable Long id,

            RedirectAttributes redirectAttributes) {

        try {

            categoryService.deactivateCategory(id);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Category deactivated successfully."
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );
        }

        return "redirect:/admin/categories";
    }


    // =====================================================
    // DELETE
    // =====================================================

    @PostMapping("/{id}/delete")
    public String deleteCategory(

            @PathVariable Long id,

            RedirectAttributes redirectAttributes) {

        try {

            categoryService.deleteCategory(id);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Category deleted successfully."
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Unable to delete category: "
                            + e.getMessage()
            );
        }

        return "redirect:/admin/categories";
    }
}