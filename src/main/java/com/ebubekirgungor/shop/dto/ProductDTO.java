package com.ebubekirgungor.shop.dto;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.ebubekirgungor.shop.model.ProductImage;
import com.ebubekirgungor.shop.model.ProductFilter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class ProductDTO {
    private String title;
    private String url;
    private double list_price;
    private short stock_quantity;
    private List<ProductImage> images;
    private List<ProductFilter> filters;
    private long category_id;
}
