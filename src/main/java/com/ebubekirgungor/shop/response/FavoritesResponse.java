package com.ebubekirgungor.shop.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoritesResponse {
    private long id;
    private String title;
    private String url;
    private double list_price;
}