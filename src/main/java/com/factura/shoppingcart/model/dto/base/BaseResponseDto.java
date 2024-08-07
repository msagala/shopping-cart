package com.factura.shoppingcart.model.dto.base;

public class BaseResponseDto <T>{
    private String status;
    private T data;

    public BaseResponseDto() {
    }

    public BaseResponseDto(String status, T data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
