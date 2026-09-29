package com.example.srikrishna.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(
            ResourceHandlerRegistry registry) {

        // ==============================
        // CATEGORY IMAGES
        // ==============================


        // ==============================
        // PRODUCT IMAGES
        // ==============================
        String uploadPath =
                System.getProperty("user.dir")
            + "/uploads/";

        registry.addResourceHandler("/products/**")
            .addResourceLocations(
                    "file:" + uploadPath + "products/"
            );


        // ==============================
        // CUSTOMER IMAGES
        // ==============================

        registry.addResourceHandler("/customers/**")
                .addResourceLocations(
                        "file:uploads/customers/"
                );


        // ==============================
        // BANNER IMAGES
        // ==============================

        registry.addResourceHandler("/banners/**")
                .addResourceLocations(
                        "file:uploads/banners/"
                );


        // ==============================
        // ADMIN IMAGES
        // ==============================

        registry.addResourceHandler("/admins/**")
                .addResourceLocations(
                        "file:uploads/admins/"
                );
    }
}
