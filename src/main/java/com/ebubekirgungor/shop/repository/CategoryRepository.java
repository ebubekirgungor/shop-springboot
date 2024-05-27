package com.ebubekirgungor.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebubekirgungor.shop.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
