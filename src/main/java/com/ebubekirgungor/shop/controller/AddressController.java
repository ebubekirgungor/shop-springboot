package com.ebubekirgungor.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebubekirgungor.shop.repository.AddressRepository;
import com.ebubekirgungor.shop.repository.UserRepository;
import com.ebubekirgungor.shop.dto.AddressDTO;
import com.ebubekirgungor.shop.exception.ResourceNotFoundException;
import com.ebubekirgungor.shop.model.Address;
import com.ebubekirgungor.shop.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/v1/addresses")
public class AddressController {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Address> getAllAddresss() {
        return addressRepository.findAll();
    }

    @PostMapping
    public Address createAddress(@RequestBody AddressDTO addressDTO) {
        Address address = new Address();
        address.setTitle(addressDTO.getTitle());
        address.setCustomer_name(addressDTO.getCustomer_name());
        address.setAddress(addressDTO.getAddress());

        User user = userRepository.findById(addressDTO.getUser_id())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        address.setUser(user);

        return addressRepository.save(address);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddressById(@PathVariable Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not exist with id :" + id));
        return ResponseEntity.ok(address);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteAddress(@PathVariable Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not exist with id :" + id));

        addressRepository.delete(address);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
