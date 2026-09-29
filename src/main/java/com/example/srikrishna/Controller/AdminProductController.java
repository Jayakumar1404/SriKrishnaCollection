package com.example.srikrishna.Controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.srikrishna.Entity.Category;
import com.example.srikrishna.Entity.Product;
import com.example.srikrishna.Entity.ProductImage;
import com.example.srikrishna.Repository.ProductImageRepository;
import com.example.srikrishna.Service.CategoryService;
import com.example.srikrishna.Service.ProductService;

@Controller
@RequestMapping("/admin")
public class AdminProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ProductImageRepository productImageRepository;

    public AdminProductController(
            ProductService productService,
            CategoryService categoryService,
            ProductImageRepository productImageRepository) {

        this.productService = productService;
        this.categoryService = categoryService;
        this.productImageRepository = productImageRepository;
    }

    // =========================================
    // PRODUCT LIST
    // =========================================

    @GetMapping("/products")
    public String products(Model model) {

        List<Product> products =
                productService.getAllProducts();

        model.addAttribute(
                "products",
                products
        );

        model.addAttribute(
                "categories",
                categoryService.getAllCategories()
        );

        model.addAttribute(
                "product",
                new Product()
        );

        return "admin/products";
    }

    // =========================================
    // SAVE PRODUCT
    // =========================================

    @PostMapping("/products/save")
    public String saveProduct(

            @ModelAttribute Product product,

            @RequestParam("categoryId")
            Long categoryId,

            @RequestParam(
                    value = "imageFiles",
                    required = false)
            List<MultipartFile> imageFiles,

            RedirectAttributes redirectAttributes)

            throws IOException {

        // =========================================
        // GET SELECTED CATEGORY
        // =========================================

        Category category =
                categoryService.getCategoryById(categoryId);

        if (category == null) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Invalid category selected!"
            );

            return "redirect:/admin/products";
        }

        // =========================================
        // SET CATEGORY TO PRODUCT
        // =========================================

        product.setCategory(category);

        // =========================================
        // UPLOAD DIRECTORY
        // =========================================

       String uploadDir =
        System.getProperty("user.dir")
        + File.separator
        + "uploads"
        + File.separator
        + "products";

File folder = new File(uploadDir);

if (!folder.exists()) {
    folder.mkdirs();
}

        // =========================================
        // SAVE PRODUCT
        // =========================================

        Product savedProduct =
                productService.saveProduct(product);

        // =========================================
        // SAVE PRODUCT IMAGES
        // =========================================

        int imageCount = 0;

        if (imageFiles != null) {

            for (MultipartFile imageFile : imageFiles) {

                if (imageFile == null
                        || imageFile.isEmpty()
                        || imageCount >= 5) {

                    continue;
                }

                String originalName =
                        imageFile.getOriginalFilename();

                if (originalName == null
                        || originalName.isBlank()) {

                    continue;
                }

                String fileName =
                        System.currentTimeMillis()
                        + "_"
                        + imageCount
                        + "_"
                        + originalName;

                imageFile.transferTo(
                        new File(folder, fileName)
                );

                // =================================
                // PRODUCT IMAGE
                // =================================

                ProductImage productImage =
                        new ProductImage();

                productImage.setImageName(
                        fileName
                );

                productImage.setProduct(
                        savedProduct
                );

                productImage.setPrimaryImage(
                        imageCount == 0
                );

                productImageRepository.save(
                        productImage
                );

                // =================================
                // FIRST IMAGE = PRIMARY IMAGE
                // =================================

                if (imageCount == 0) {

                    savedProduct.setImage(
                            fileName
                    );

                    productService.updateProduct(
                            savedProduct
                    );
                }

                imageCount++;
            }
        }

        // =========================================
        // SUCCESS MESSAGE
        // =========================================

        redirectAttributes.addFlashAttribute(
                "success",
                imageCount > 0
                        ? "Product added successfully with "
                                + imageCount
                                + " image(s)!"
                        : "Product added successfully!"
        );

        return "redirect:/admin/products";
    }

    // =========================================
    // DELETE PRODUCT
    // =========================================

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {

            productService.deleteProduct(id);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Product deleted successfully!"
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Unable to delete product: "
                            + e.getMessage()
            );
        }

        return "redirect:/admin/products";
    }

    // =========================================
    // EDIT PRODUCT
    // =========================================

    @GetMapping("/products/edit/{id}")
    public String editProduct(
            @PathVariable Long id,
            Model model) {

        Product product =
                productService.getProductById(id);

        if (product == null) {

            return "redirect:/admin/products";
        }

        List<ProductImage> productImages =
                productImageRepository
                        .findByProductId(id);

        model.addAttribute(
                "product",
                product
        );

        model.addAttribute(
                "productImages",
                productImages
        );

        model.addAttribute(
                "products",
                productService.getAllProducts()
        );

        model.addAttribute(
                "categories",
                categoryService.getAllCategories()
        );

        return "admin/products";
    }

    // =========================================
    // UPDATE PRODUCT
    // =========================================

    @PostMapping("/products/update")
    public String updateProduct(

            @ModelAttribute Product product,

            @RequestParam("categoryId")
            Long categoryId,

            @RequestParam(
                    value = "imageFiles",
                    required = false)
            List<MultipartFile> imageFiles)

            throws IOException {

        // =========================================
        // GET CATEGORY
        // =========================================

        Category category =
                categoryService.getCategoryById(
                        categoryId
                );

        if (category == null) {

            return "redirect:/admin/products";
        }

        // =========================================
        // SET CATEGORY
        // =========================================

        product.setCategory(category);

        // =========================================
        // GET EXISTING PRODUCT
        // =========================================

        Product oldProduct =
                productService.getProductById(
                        product.getId()
                );

        if (oldProduct == null) {

            return "redirect:/admin/products";
        }

        // =========================================
        // KEEP EXISTING PRIMARY IMAGE
        // =========================================

        product.setImage(
                oldProduct.getImage()
        );

        // =========================================
        // UPLOAD DIRECTORY
        // =========================================

    String uploadDir =
        System.getProperty("user.dir")
        + File.separator
        + "uploads"
        + File.separator
        + "products";

File folder = new File(uploadDir);

if (!folder.exists()) {
    folder.mkdirs();
}
        // =========================================
        // UPDATE PRODUCT
        // =========================================

        Product updatedProduct =
                productService.updateProduct(
                        product
                );

        // =========================================
        // ADD NEW IMAGES
        // =========================================

        if (imageFiles != null
                && !imageFiles.isEmpty()) {

            int existingImageCount =
                    productImageRepository
                            .findByProductId(
                                    product.getId()
                            )
                            .size();

            int remainingSlots =
                    Math.max(
                            0,
                            5 - existingImageCount
                    );

            int addedCount = 0;

            for (MultipartFile imageFile
                    : imageFiles) {

                if (addedCount >= remainingSlots) {
                    break;
                }

                if (imageFile == null
                        || imageFile.isEmpty()) {

                    continue;
                }

                String originalName =
                        imageFile.getOriginalFilename();

                if (originalName == null
                        || originalName.isBlank()) {

                    continue;
                }

                String fileName =
                        System.currentTimeMillis()
                        + "_"
                        + originalName;

                File destination =
                        new File(
                                folder,
                                fileName
                        );

                imageFile.transferTo(
                        destination
                );

                // =================================
                // PRODUCT IMAGE
                // =================================

                ProductImage productImage =
                        new ProductImage();

                productImage.setImageName(
                        fileName
                );

                productImage.setProduct(
                        updatedProduct
                );

                productImage.setPrimaryImage(
                        false
                );

                productImageRepository.save(
                        productImage
                );

                // =================================
                // IF NO PRIMARY IMAGE
                // =================================

                if (updatedProduct.getImage() == null
                        || updatedProduct.getImage()
                                .isBlank()) {

                    updatedProduct.setImage(
                            fileName
                    );

                    productService.updateProduct(
                            updatedProduct
                    );
                }

                addedCount++;
            }
        }

        return "redirect:/admin/products";
    }

    // =========================================
    // DELETE PRODUCT IMAGE
    // =========================================

    @GetMapping("/products/image/delete/{imageId}")
    public String deleteProductImage(
            @PathVariable Long imageId)
            throws IOException {

        ProductImage productImage =
                productImageRepository
                        .findById(imageId)
                        .orElse(null);

        if (productImage == null) {

            return "redirect:/admin/products";
        }

        Product product =
                productImage.getProduct();

        Long productId =
                product.getId();

        // =========================================
        // DELETE PHYSICAL IMAGE
        // =========================================

        String uploadDir =
                "uploads/products/";

        File imageFile =
                new File(
                        uploadDir
                                + productImage.getImageName()
                );

        if (imageFile.exists()) {

            imageFile.delete();
        }

        // =========================================
        // DELETE DATABASE RECORD
        // =========================================

        productImageRepository.deleteById(
                imageId
        );

        return "redirect:/admin/products/edit/"
                + productId;
    }

    // =========================================
    // MAKE IMAGE PRIMARY
    // =========================================

    @GetMapping("/products/image/primary/{imageId}")
    public String makePrimary(
            @PathVariable Long imageId) {

        ProductImage selectedImage =
                productImageRepository
                        .findById(imageId)
                        .orElse(null);

        if (selectedImage == null) {

            return "redirect:/admin/products";
        }

        Product product =
                selectedImage.getProduct();

        Long productId =
                product.getId();

        // =========================================
        // GET ALL PRODUCT IMAGES
        // =========================================

        List<ProductImage> images =
                productImageRepository
                        .findByProductId(productId);

        // =========================================
        // REMOVE PRIMARY FROM ALL
        // =========================================

        for (ProductImage image : images) {

            image.setPrimaryImage(false);
        }

        productImageRepository.saveAll(
                images
        );

        // =========================================
        // MAKE SELECTED IMAGE PRIMARY
        // =========================================

        selectedImage.setPrimaryImage(true);

        productImageRepository.save(
                selectedImage
        );

        // =========================================
        // UPDATE PRODUCT IMAGE
        // =========================================

        product.setImage(
                selectedImage.getImageName()
        );

        productService.updateProduct(
                product
        );

        return "redirect:/admin/products/edit/"
                + productId;
    }
}
