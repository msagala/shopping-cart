package com.factura.shoppingcart.service;

import com.factura.shoppingcart.exception.CartNotFoundException;
import com.factura.shoppingcart.exception.ItemNotFoundException;
import com.factura.shoppingcart.model.dto.CartDto;
import com.factura.shoppingcart.model.entity.CartEntity;
import com.factura.shoppingcart.model.dto.ItemDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {

    public CartDto addItemToCart(Long cartId, ItemDto item) throws CartNotFoundException;
    public CartDto removeItemToCart(Long cartId, Long itemId) throws CartNotFoundException, ItemNotFoundException;
    public CartDto updateItemToCart(Long cartId, ItemDto item) throws CartNotFoundException, ItemNotFoundException;
    public CartDto getCartItemsById(Long cartId) throws CartNotFoundException;

    public List<CartDto> getCarts();
    public void removeCartById(long cartId) throws CartNotFoundException;

}
