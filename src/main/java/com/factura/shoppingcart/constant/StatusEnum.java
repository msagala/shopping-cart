package com.factura.shoppingcart.constant;

public enum StatusEnum {
    SUCCESS("Success"),FAIL("Fail");
    private String status;
    private StatusEnum(String status) {
        this.status = status;
    }
    public String getStatus() {
        return this.status;
    }
}
