package com.ebubekirgungor.shop.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    public static enum Role {
        CUSTOMER,
        ADMIN
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Cart {
        private long id;
        private byte quantity;
        private Boolean selected;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CartDTO {
        private List<Cart> cart;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String first_name;

    @Column(name = "last_name", nullable = false)
    private String last_name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE)
    private Date birth_date;

    @Column(name = "gender", nullable = false)
    private Boolean gender;

    @Column(name = "role", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Address> addresses;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "cart", columnDefinition = "jsonb", nullable = false)
    private List<Cart> cart;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
            CascadeType.REFRESH })
    @JoinTable(name = "user_products", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> favorites;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();

        list.add(new SimpleGrantedAuthority(role.name()));

        return list;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegisterUserDto {
        private String email;
        private String password;
        private String first_name;
        private String last_name;
        private String phone;
        private Date birth_date;
        private Boolean gender;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginUserDto {
        private String email;
        private String password;
        private Boolean remember_me;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateUserDto {
        private String first_name;
        private String last_name;
        private String phone;
        private Date birth_date;
        private Boolean gender;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdatePasswordDto {
        private String old_password;
        private String new_password;
    }
}
