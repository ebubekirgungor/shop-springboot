package com.ebubekirgungor.shop.response;

import java.util.List;

import com.ebubekirgungor.shop.model.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
    private String title;
    private String[] filters;
    private List<Product> products;
}