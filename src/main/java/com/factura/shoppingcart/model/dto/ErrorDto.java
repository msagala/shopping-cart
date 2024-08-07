package com.factura.shoppingcart.model.dto;


import java.time.LocalDateTime;

public class ErrorDto {

    private String message;
    private String details;

    private LocalDateTime timeStamp;

    public ErrorDto() {
    }


    public ErrorDto(String message, String details, LocalDateTime timeStamp) {
        this.message = message;
        this.details = details;
        this.timeStamp = timeStamp;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
