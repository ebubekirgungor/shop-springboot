package com.ebubekirgungor.shop.model;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
class Cart {
    private long id;
    private byte quantity;
    private boolean selected;
}

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String first_name;

    @Column(name = "last_name")
    private String last_name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "birth_date")
    private String birth_date;

    @Column(name = "gender")
    private boolean gender;

    @Column(name = "role")
    private byte role;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "cart", columnDefinition = "jsonb")
    private Cart[] cart;
}
