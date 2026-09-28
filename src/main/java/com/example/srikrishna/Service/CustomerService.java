package com.example.srikrishna.Service;

import java.time.LocalDateTime;
import java.util.List;

import com.example.srikrishna.Entity.Customer;

public interface CustomerService {

        List<Customer> getAllCustomers();

        Customer getCustomerById(Long id);

        Customer getCustomerByEmail(String email);

        List<Customer> searchCustomers(String keyword);

        List<Customer> getCustomersByStatus(String status);

        List<Customer> searchAndFilterCustomers(
                        String keyword,
                        String status);

        Customer saveCustomer(Customer customer);

        Customer updateCustomer(Customer customer);

        void activateCustomer(Long id);

        void deactivateCustomer(Long id);

        void deleteCustomer(Long id);

        long getTotalCustomers();

        long getActiveCustomers();

        long getInactiveCustomers();

        long getTodayCustomers(
                        LocalDateTime start,
                        LocalDateTime end);

        Customer updateProfile(
                        Long customerId,
                        String firstName,
                        String lastName,
                        String mobile,
                        String gender,
                        String address,
                        String city,
                        String state,
                        String pincode);

        Customer updateProfileImage(
                        Long customerId,
                        org.springframework.web.multipart.MultipartFile image);

        Customer removeProfileImage(Long customerId);

        void changePassword(
                        Long customerId,
                        String currentPassword,
                        String newPassword,
                        String confirmPassword);
}