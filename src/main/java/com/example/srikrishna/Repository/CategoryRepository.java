package com.example.srikrishna.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.srikrishna.Entity.Category;

public interface CategoryRepository
        extends JpaRepository<Category, Long> {


    Optional<Category> findByNameIgnoreCase(
            String name
    );


    boolean existsByNameIgnoreCase(
            String name
    );


    List<Category> findAllByOrderByCreatedAtDesc();


    List<Category> findByStatusOrderByCreatedAtDesc(
            Boolean status
    );


    List<Category> findByNameContainingIgnoreCaseOrderByCreatedAtDesc(
            String name
    );


    List<Category>
    findByNameContainingIgnoreCaseAndStatusOrderByCreatedAtDesc(
            String name,
            Boolean status
    );


    long countByStatus(Boolean status);

}