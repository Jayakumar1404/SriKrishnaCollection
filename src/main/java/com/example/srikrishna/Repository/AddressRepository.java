package com.example.srikrishna.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.srikrishna.Entity.Address;

public interface AddressRepository
        extends JpaRepository<Address,Long>{

    List<Address> findByCustomerId(Long customerId);

}