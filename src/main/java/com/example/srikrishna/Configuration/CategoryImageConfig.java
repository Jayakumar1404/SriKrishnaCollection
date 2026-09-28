package com.example.srikrishna.Configuration;

import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CategoryImageConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        /*
         * ROOT UPLOADS
         *
         * uploads/categories/
         */
        String rootCategoryPath =
                Paths.get("uploads", "categories")
                        .toAbsolutePath()
                        .normalize()
                        .toUri()
                        .toString();

        if (!rootCategoryPath.endsWith("/")) {
            rootCategoryPath += "/";
        }


        /*
         * STATIC UPLOADS
         *
         * src/main/resources/static/uploads/categories/
         */
        String staticCategoryPath =
                Paths.get(
                        "src",
                        "main",
                        "resources",
                        "static",
                        "uploads",
                        "categories"
                )
                .toAbsolutePath()
                .normalize()
                .toUri()
                .toString();

        if (!staticCategoryPath.endsWith("/")) {
            staticCategoryPath += "/";
        }


        System.out.println(
                "ROOT CATEGORY IMAGE PATH = "
                + rootCategoryPath
        );

        System.out.println(
                "STATIC CATEGORY IMAGE PATH = "
                + staticCategoryPath
        );


        /*
         * CUSTOMER CATEGORY IMAGE URL
         */
        registry.addResourceHandler(
                "/categories/**"
        )
        .addResourceLocations(
                rootCategoryPath,
                staticCategoryPath,
                "classpath:/static/uploads/categories/"
        );


        /*
         * OLD / EXISTING URL
         */
        registry.addResourceHandler(
                "/category-images/**"
        )
        .addResourceLocations(
                rootCategoryPath,
                staticCategoryPath,
                "classpath:/static/uploads/categories/"
        );
    }
}