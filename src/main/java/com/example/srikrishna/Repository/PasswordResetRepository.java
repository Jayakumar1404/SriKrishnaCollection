package com.example.srikrishna.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.srikrishna.Entity.PasswordReset;

public interface PasswordResetRepository
        extends JpaRepository<PasswordReset, Long> {

    Optional<PasswordReset> findByEmail(String email);

    void deleteByEmail(String email);
}