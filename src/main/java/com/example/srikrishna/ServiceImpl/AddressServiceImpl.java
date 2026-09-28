package com.example.srikrishna.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.srikrishna.Entity.Address;
import com.example.srikrishna.Repository.AddressRepository;
import com.example.srikrishna.Service.AddressService;

@Service
public class AddressServiceImpl
        implements AddressService {

    private final AddressRepository repository;

    public AddressServiceImpl(AddressRepository repository) {

        this.repository = repository;

    }

    @Override
    public Address save(Address address) {

        return repository.save(address);

    }

    @Override
    public List<Address> getAddress(Long customerId) {

        return repository.findByCustomerId(customerId);

    }

}