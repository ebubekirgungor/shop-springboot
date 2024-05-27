package com.ebubekirgungor.shop.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class AddressDTO {
    private String title;
    private String customer_name;
    private String address;
    private long user_id;
}
