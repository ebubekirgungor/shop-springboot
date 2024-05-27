package com.ebubekirgungor.shop.model;

import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

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
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "url")
    private String url;

    @Column(name = "list_price")
    private double list_price;

    @Column(name = "stock_quantity")
    private short stock_quantity;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "images", columnDefinition = "jsonb")
    private List<ProductImage> images;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "filters", columnDefinition = "jsonb")
    private List<ProductFilter> filters;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category category;

    @JsonProperty("category")
    public String getCategoryTitle() {
        return category != null ? category.getTitle() : null;
    }
}
