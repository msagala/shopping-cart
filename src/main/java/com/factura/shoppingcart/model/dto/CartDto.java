package com.factura.shoppingcart.model.dto;

import com.factura.shoppingcart.model.entity.ItemEntity;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CartDto {

    private Long id;
    private BigDecimal totalPrice;
    private Set<ItemEntity> items = new HashSet<>();

    public CartDto() {
    }

    public CartDto(Long id, Set<ItemEntity> itemEntitySet, BigDecimal totalPrice) {
        this.id = id;
        this.items = itemEntitySet;
        this.totalPrice = totalPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<ItemEntity> getItems() {
        return items;
    }

    public void setItems(Set<ItemEntity> items) {
        this.items = items;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
