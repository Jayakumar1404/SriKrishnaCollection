package com.example.srikrishna.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.srikrishna.Entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Find customer by email
    Optional<Customer> findByEmail(String email);


    // Search customers
    @Query("""
        SELECT c
        FROM Customer c
        WHERE
            LOWER(c.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(c.mobile) LIKE LOWER(CONCAT('%', :keyword, '%'))
        ORDER BY c.createdAt DESC
        """)
    List<Customer> searchCustomers(
            @Param("keyword") String keyword
    );


    // Status filter
    List<Customer> findByStatusOrderByCreatedAtDesc(
            Boolean status
    );


    // Search + status
    @Query("""
        SELECT c
        FROM Customer c
        WHERE
            (
                LOWER(c.firstName) LIKE
                LOWER(CONCAT('%', :keyword, '%'))

                OR LOWER(c.lastName) LIKE
                LOWER(CONCAT('%', :keyword, '%'))

                OR LOWER(c.email) LIKE
                LOWER(CONCAT('%', :keyword, '%'))

                OR LOWER(c.mobile) LIKE
                LOWER(CONCAT('%', :keyword, '%'))
            )
            AND
            (
                :status = 'ALL'

                OR (
                    :status = 'ACTIVE'
                    AND c.status = true
                )

                OR (
                    :status = 'INACTIVE'
                    AND c.status = false
                )
            )
        ORDER BY c.createdAt DESC
        """)
    List<Customer> searchAndFilterCustomers(
            @Param("keyword") String keyword,
            @Param("status") String status
    );


    // Today's customers
    long countByCreatedAtBetween(
            java.time.LocalDateTime start,
            java.time.LocalDateTime end
    );


    // Active / inactive
    long countByStatus(Boolean status);


    // Email exists
    boolean existsByEmail(String email);


    // Mobile exists
    boolean existsByMobile(String mobile);
    Optional<Customer> findByMobile(String mobile);
}