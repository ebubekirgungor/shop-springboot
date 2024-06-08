package com.ebubekirgungor.shop.response;

import java.util.List;

import com.ebubekirgungor.shop.model.Product.ProductFilter;
import com.ebubekirgungor.shop.model.Product.ProductImage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private long id;
    private String title;
    private String url;
    private double list_price;
    private short stock_quantity;
    private List<ProductImage> images;
    private List<ProductFilter> filters;
    private String category;
    private Boolean is_favorite;
}