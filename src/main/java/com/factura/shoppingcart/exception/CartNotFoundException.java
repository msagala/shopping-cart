package com.factura.shoppingcart.exception;

public class CartNotFoundException extends Exception {

    public CartNotFoundException(String message) {
        super(message);
    }

    public CartNotFoundException() {
        super("Cart not found!");
    }

}
