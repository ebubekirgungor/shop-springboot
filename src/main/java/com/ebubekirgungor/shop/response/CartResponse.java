package com.ebubekirgungor.shop.response;

import java.util.List;

import com.ebubekirgungor.shop.model.Product.ProductImage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    private long id;
    private String title;
    private String url;
    private double list_price;
    private short stock_quantity;
    private List<ProductImage> images;
    private byte cart_quantity;
    private Boolean selected;
}