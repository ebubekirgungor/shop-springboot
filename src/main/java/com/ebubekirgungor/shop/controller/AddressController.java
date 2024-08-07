package com.ebubekirgungor.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebubekirgungor.shop.repository.AddressRepository;
import com.ebubekirgungor.shop.exception.ResourceNotFoundException;
import com.ebubekirgungor.shop.model.Address;
import com.ebubekirgungor.shop.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "${origin-url}", allowCredentials = "true")
@RestController
@RequestMapping("${api-base}/addresses")
public class AddressController {

    @Autowired
    private AddressRepository addressRepository;

    @GetMapping
    public List<Address> getAllAddresses() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return addressRepository.findByUserId(((User) authentication.getPrincipal()).getId());
    }

    @PostMapping
    public Address createAddress(@RequestBody Address address) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        address.setUser((User) authentication.getPrincipal());

        return addressRepository.save(address);
    }

    @PutMapping("/{id}")
    public Address updateAddress(@RequestBody Address address, @PathVariable Long id) {
        Address oldAddress = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not exist with id :" + id));

        oldAddress.setTitle(address.getTitle());
        oldAddress.setCustomer_name(address.getCustomer_name());
        oldAddress.setAddress(address.getAddress());

        return addressRepository.save(oldAddress);
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
