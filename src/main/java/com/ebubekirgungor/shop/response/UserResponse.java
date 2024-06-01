package com.ebubekirgungor.shop.response;

import java.util.List;

import com.ebubekirgungor.shop.model.User.Cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String email;
    private String first_name;
    private String last_name;
    private String phone;
    private String birth_date;
    private Boolean gender;
    private List<Cart> cart;
}