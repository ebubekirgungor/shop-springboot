package com.ebubekirgungor.shop.model;

import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
class OrderProduct {
    private String url;
    private String image;
    private byte quantity;
    private double list_price;
}

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {

    public static enum DeliveryStatus {
        Delivered((byte) 0),
        InProgress((byte) 1),
        Returned((byte) 2),
        Canceled((byte) 3);

        private final byte value;

        DeliveryStatus(final byte value) {
            this.value = value;
        }

        public byte getValue() {
            return value;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "total_amount", nullable = false)
    private double total_amount;

    @Column(name = "customer_name", nullable = false)
    private String customer_name;

    @Column(name = "delivery_address", nullable = false)
    private String delivery_address;

    @Column(name = "delivery_status", nullable = false)
    private byte delivery_status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "products", columnDefinition = "jsonb", nullable = false)
    private List<OrderProduct> products;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderDTO {
        private double total_amount;
        private String customer_name;
        private String delivery_address;
        private DeliveryStatus delivery_status;
        private List<OrderProduct> products;
        private long user_id;
    }
}
