package com.ebubekirgungor.shop.response;

import com.ebubekirgungor.shop.model.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse extends Product {
    private byte cart_quantity;
    private Boolean selected;
}