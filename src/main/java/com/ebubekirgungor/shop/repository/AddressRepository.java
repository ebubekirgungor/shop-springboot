package com.ebubekirgungor.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebubekirgungor.shop.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}
