package com.example.srikrishna.ServiceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.srikrishna.Entity.Customer;
import com.example.srikrishna.Repository.CustomerRepository;
import com.example.srikrishna.Service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder;

    // =====================================================
    // PROFILE IMAGE LOCATION
    // =====================================================

    private final String uploadDirectory =
            "uploads/customer-images/";


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public CustomerServiceImpl(
            CustomerRepository customerRepository,
            PasswordEncoder passwordEncoder) {

        this.customerRepository =
                customerRepository;

        this.passwordEncoder =
                passwordEncoder;
    }


    // =====================================================
    // GET ALL CUSTOMERS
    // =====================================================

    @Override
    public List<Customer> getAllCustomers() {

        return customerRepository.findAll();
    }


    // =====================================================
    // GET CUSTOMER BY ID
    // =====================================================

    @Override
    public Customer getCustomerById(Long id) {

        return customerRepository
                .findById(id)
                .orElse(null);
    }


    // =====================================================
    // SAVE CUSTOMER
    // =====================================================

   @Override
public Customer saveCustomer(Customer customer) {

    // Encode password before saving
    if (customer.getPassword() != null &&
            !customer.getPassword().isBlank()) {

        customer.setPassword(
                passwordEncoder.encode(
                        customer.getPassword()
                )
        );
    }

    return customerRepository.save(customer);
}


    // =====================================================
    // UPDATE CUSTOMER
    // =====================================================

    @Override
    public Customer updateCustomer(
            Customer customer) {

        return customerRepository.save(
                customer
        );
    }


    // =====================================================
    // GET CUSTOMER BY EMAIL
    // =====================================================

    @Override
    public Customer getCustomerByEmail(
            String email) {

        return customerRepository
                .findByEmail(email)
                .orElse(null);
    }


    // =====================================================
    // SEARCH CUSTOMERS
    // =====================================================

    @Override
    public List<Customer> searchCustomers(
            String keyword) {

        if (keyword == null ||
                keyword.trim().isEmpty()) {

            return customerRepository.findAll();
        }

        return customerRepository.searchCustomers(
                keyword.trim()
        );
    }


    // =====================================================
    // GET CUSTOMERS BY STATUS
    // =====================================================

    @Override
    public List<Customer> getCustomersByStatus(
            String status) {

        if (status == null ||
                status.trim().isEmpty() ||
                status.equalsIgnoreCase("ALL")) {

            return customerRepository.findAll();
        }

        if (status.equalsIgnoreCase("ACTIVE")) {

            return customerRepository
                    .findByStatusOrderByCreatedAtDesc(
                            true
                    );
        }

        if (status.equalsIgnoreCase("INACTIVE")) {

            return customerRepository
                    .findByStatusOrderByCreatedAtDesc(
                            false
                    );
        }

        return customerRepository.findAll();
    }


    // =====================================================
    // SEARCH + FILTER
    // =====================================================

    @Override
    public List<Customer> searchAndFilterCustomers(
            String keyword,
            String status) {

        String searchKeyword =
                keyword == null
                        ? ""
                        : keyword.trim();

        String searchStatus =
                status == null ||
                status.trim().isEmpty()
                        ? "ALL"
                        : status.trim()
                            .toUpperCase();

        return customerRepository
                .searchAndFilterCustomers(
                        searchKeyword,
                        searchStatus
                );
    }


    // =====================================================
    // ACTIVATE CUSTOMER
    // =====================================================

    @Override
    public void activateCustomer(
            Long id) {

        Customer customer =
                customerRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Customer not found"
                                )
                        );

        customer.setStatus(true);

        customerRepository.save(
                customer
        );
    }


    // =====================================================
    // DEACTIVATE CUSTOMER
    // =====================================================

    @Override
    public void deactivateCustomer(
            Long id) {

        Customer customer =
                customerRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Customer not found"
                                )
                        );

        customer.setStatus(false);

        customerRepository.save(
                customer
        );
    }


    // =====================================================
    // DELETE CUSTOMER
    // =====================================================

    @Override
    public void deleteCustomer(
            Long id) {

        if (!customerRepository.existsById(id)) {

            throw new RuntimeException(
                    "Customer not found"
            );
        }

        customerRepository.deleteById(id);
    }


    // =====================================================
    // TOTAL CUSTOMERS
    // =====================================================

    @Override
    public long getTotalCustomers() {

        return customerRepository.count();
    }


    // =====================================================
    // ACTIVE CUSTOMERS
    // =====================================================

    @Override
    public long getActiveCustomers() {

        return customerRepository.countByStatus(
                true
        );
    }


    // =====================================================
    // INACTIVE CUSTOMERS
    // =====================================================

    @Override
    public long getInactiveCustomers() {

        return customerRepository.countByStatus(
                false
        );
    }


    // =====================================================
    // TODAY CUSTOMERS
    // =====================================================

    @Override
    public long getTodayCustomers(
            LocalDateTime start,
            LocalDateTime end) {

        return customerRepository
                .countByCreatedAtBetween(
                        start,
                        end
                );
    }


    // =====================================================
    // UPDATE PROFILE
    // =====================================================

    @Override
    public Customer updateProfile(

            Long customerId,

            String firstName,

            String lastName,

            String mobile,

            String gender,

            String address,

            String city,

            String state,

            String pincode) {

        Customer customer =
                customerRepository
                        .findById(customerId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Customer not found."
                                )
                        );


        // =================================================
        // FIRST NAME
        // =================================================

        if (firstName == null ||
                firstName.trim().isEmpty()) {

            throw new RuntimeException(
                    "First name is required."
            );
        }


        // =================================================
        // LAST NAME
        // =================================================

        if (lastName == null ||
                lastName.trim().isEmpty()) {

            throw new RuntimeException(
                    "Last name is required."
            );
        }


        // =================================================
        // MOBILE
        // =================================================

        if (mobile == null ||
                !mobile.matches("\\d{10}")) {

            throw new RuntimeException(
                    "Mobile number must contain exactly 10 digits."
            );
        }


        // =================================================
        // CHECK DUPLICATE MOBILE
        // =================================================

        customerRepository
                .findByMobile(mobile)
                .ifPresent(existing -> {

                    if (!existing.getId()
                            .equals(customerId)) {

                        throw new RuntimeException(
                                "This mobile number is already registered."
                        );
                    }
                });


        // =================================================
        // PINCODE
        // =================================================

        if (pincode != null &&
                !pincode.trim().isEmpty() &&
                !pincode.matches("\\d{6}")) {

            throw new RuntimeException(
                    "Pincode must contain exactly 6 digits."
            );
        }


        // =================================================
        // UPDATE
        // =================================================

        customer.setFirstName(
                firstName.trim()
        );

        customer.setLastName(
                lastName.trim()
        );

        customer.setMobile(
                mobile.trim()
        );

        customer.setGender(
                gender == null
                        ? ""
                        : gender.trim()
        );

        customer.setAddress(
                address == null
                        ? ""
                        : address.trim()
        );

        customer.setCity(
                city == null
                        ? ""
                        : city.trim()
        );

        customer.setState(
                state == null
                        ? ""
                        : state.trim()
        );

        customer.setPincode(
                pincode == null
                        ? ""
                        : pincode.trim()
        );


        return customerRepository.save(
                customer
        );
    }


    // =====================================================
    // UPLOAD PROFILE IMAGE
    // =====================================================

    @Override
    public Customer updateProfileImage(

            Long customerId,

            MultipartFile image) {

        Customer customer =
                customerRepository
                        .findById(customerId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Customer not found."
                                )
                        );


        // =================================================
        // EMPTY FILE
        // =================================================

        if (image == null ||
                image.isEmpty()) {

            throw new RuntimeException(
                    "Please select an image."
            );
        }


        // =================================================
        // IMAGE TYPE
        // =================================================

        String contentType =
                image.getContentType();

        if (contentType == null ||
                !contentType.equals("image/jpeg") &&
                !contentType.equals("image/png") &&
                !contentType.equals("image/webp")) {

            throw new RuntimeException(
                    "Only JPG, PNG and WEBP images are allowed."
            );
        }


        // =================================================
        // IMAGE SIZE
        // =================================================

        if (image.getSize() >
                5 * 1024 * 1024) {

            throw new RuntimeException(
                    "Image size must be less than 5 MB."
            );
        }


        try {

            Path uploadPath =
                    Paths.get(
                            uploadDirectory
                    );

            Files.createDirectories(
                    uploadPath
            );


            // =============================================
            // DELETE OLD IMAGE
            // =============================================

            if (customer.getImage() != null &&
                    !customer.getImage().isBlank()) {

                Path oldImage =
                        uploadPath.resolve(
                                customer.getImage()
                        );

                Files.deleteIfExists(
                        oldImage
                );
            }


            // =============================================
            // GET EXTENSION
            // =============================================

            String originalName =
                    image.getOriginalFilename();

            String extension = "";

            if (originalName != null &&
                    originalName.contains(".")) {

                extension =
                        originalName.substring(
                                originalName.lastIndexOf(".")
                        );
            }


            // =============================================
            // UNIQUE FILE NAME
            // =============================================

            String fileName =
                    UUID.randomUUID()
                            .toString()
                            + extension;


            Path filePath =
                    uploadPath.resolve(
                            fileName
                    );


            // =============================================
            // SAVE FILE
            // =============================================

            Files.copy(
                    image.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );


            // =============================================
            // SAVE DATABASE
            // =============================================

            customer.setImage(
                    fileName
            );


            return customerRepository.save(
                    customer
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to upload profile image.",
                    e
            );
        }
    }


    // =====================================================
    // REMOVE PROFILE IMAGE
    // =====================================================

    @Override
    public Customer removeProfileImage(
            Long customerId) {

        Customer customer =
                customerRepository
                        .findById(customerId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Customer not found."
                                )
                        );


        try {

            if (customer.getImage() != null &&
                    !customer.getImage().isBlank()) {

                Path imagePath =
                        Paths.get(
                                uploadDirectory,
                                customer.getImage()
                        );

                Files.deleteIfExists(
                        imagePath
                );
            }

        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to remove profile image.",
                    e
            );
        }


        customer.setImage(null);


        return customerRepository.save(
                customer
        );
    }


    // =====================================================
    // CHANGE PASSWORD
    // =====================================================

    @Override
    public void changePassword(

            Long customerId,

            String currentPassword,

            String newPassword,

            String confirmPassword) {

        Customer customer =
                customerRepository
                        .findById(customerId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Customer not found."
                                )
                        );


        // =================================================
        // CURRENT PASSWORD
        // =================================================

        if (currentPassword == null ||
                currentPassword.isBlank()) {

            throw new RuntimeException(
                    "Current password is required."
            );
        }


        // =================================================
        // CHECK CURRENT PASSWORD
        // =================================================

        if (customer.getPassword() == null ||
                !passwordEncoder.matches(
                        currentPassword,
                        customer.getPassword()
                )) {

            throw new RuntimeException(
                    "Current password is incorrect."
            );
        }


        // =================================================
        // NEW PASSWORD
        // =================================================

        if (newPassword == null ||
                newPassword.length() < 8) {

            throw new RuntimeException(
                    "New password must contain at least 8 characters."
            );
        }


        // =================================================
        // CONFIRM PASSWORD
        // =================================================

        if (!newPassword.equals(
                confirmPassword
        )) {

            throw new RuntimeException(
                    "New password and confirm password do not match."
            );
        }


        // =================================================
        // SAME PASSWORD CHECK
        // =================================================

        if (passwordEncoder.matches(
                newPassword,
                customer.getPassword()
        )) {

            throw new RuntimeException(
                    "New password must be different from the current password."
            );
        }


        // =================================================
        // ENCODE PASSWORD
        // =================================================

        customer.setPassword(
                passwordEncoder.encode(
                        newPassword
                )
        );


        customerRepository.save(
                customer
        );
    }
}