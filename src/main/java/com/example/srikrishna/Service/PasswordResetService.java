package com.example.srikrishna.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.srikrishna.Repository.PasswordResetRepository;

@Service
public class PasswordResetService {

    private final PasswordResetRepository repository;

    public PasswordResetService(
            PasswordResetRepository repository) {

        this.repository = repository;
    }

    @Transactional
    public void deleteByEmail(String email) {

        repository.deleteByEmail(email);
    }
}