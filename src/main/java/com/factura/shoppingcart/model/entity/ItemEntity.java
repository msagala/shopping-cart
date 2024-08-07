package com.factura.shoppingcart.model.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "item_table")
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "item_code")
    private String itemCode;
    private String name;
    private String category;
    private Integer quantity;
    private BigDecimal price;
    @Column(name = "total_price")
    private BigDecimal totalPrice;


    public ItemEntity() {
    }

    public ItemEntity(Long id, String itemCode, String name, String category, Integer quantity,
                      BigDecimal price, BigDecimal totalPrice) {
        this.id = id;
        this.itemCode = itemCode;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
