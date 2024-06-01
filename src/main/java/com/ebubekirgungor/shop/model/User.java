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
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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
        Customer((byte) 0),
        Admin((byte) 1);

        private final byte value;

        Role(final byte value) {
            this.value = value;
        }

        public byte getValue() {
            return value;
        }
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
    private String birth_date;

    @Column(name = "gender", nullable = false)
    private Boolean gender;

    @Column(name = "role", nullable = false)
    private byte role;

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

    @JsonProperty("favorites_ids")
    public List<Long> getFavoritesIds() {
        List<Long> ids = new ArrayList<>();

        for (Product product : favorites) {
            ids.add(product.getId());
        }

        return favorites != null ? ids : new ArrayList<Long>();
    }

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
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
        private String birth_date;
        private Boolean gender;
        private Role role;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginUserDto {
        private String email;
        private String password;
        private Boolean remember_me;
    }
}
