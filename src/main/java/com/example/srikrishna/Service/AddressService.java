package com.example.srikrishna.Service;

import java.util.List;

import com.example.srikrishna.Entity.Address;

public interface AddressService {

    Address save(Address address);

    List<Address> getAddress(Long customerId);

}