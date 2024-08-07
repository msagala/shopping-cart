package com.factura.shoppingcart.exception;

public class ItemNotFoundException extends Exception{
    public ItemNotFoundException(String message) {
        super(message);
    }

    public ItemNotFoundException() {
        super("Item not found!");
    }
}
