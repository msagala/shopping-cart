package com.factura.shoppingcart.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ItemDto {

    private Long cartId;
    @NotBlank(message = "item_code is mandatory")
    @Size(min=2, max=15, message = "item_code size must be between 2 and 15")
    private String itemCode;
    @NotBlank(message = "item_name is mandatory")
    @Size(min=2, max=50, message = "item_name size must be between 2 and 50")
    private String itemName;
    @NotBlank(message = "item_category is mandatory")
    @Size(min=2, max=50, message = "item_category size must be between 2 and 50")
    private String itemCategory;
    @NotNull(message = "item_quantity is mandatory")
    private Integer itemQuantity;
    @NotNull(message = "item_price is mandatory")
    private BigDecimal itemPrice;

    public ItemDto() {
    }

    public ItemDto(Long cartId, String itemCode, String itemName, String itemCategory, Integer itemQuantity, BigDecimal itemPrice) {
        this.cartId = cartId;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.itemCategory = itemCategory;
        this.itemQuantity = itemQuantity;
        this.itemPrice = itemPrice;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public Integer getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }
}
