package com.factura.shoppingcart.controller;

import com.factura.shoppingcart.constant.StatusEnum;
import com.factura.shoppingcart.exception.CartNotFoundException;
import com.factura.shoppingcart.exception.ItemNotFoundException;
import com.factura.shoppingcart.model.dto.CartDto;
import com.factura.shoppingcart.model.dto.ItemDto;
import com.factura.shoppingcart.model.dto.base.BaseResponseDto;
import com.factura.shoppingcart.service.CartService;
import com.factura.shoppingcart.util.MapperUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/shopping-cart")
public class CartController {
    private CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/carts")
    public ResponseEntity<BaseResponseDto<List<CartDto>>> getCarts() {
        return ResponseEntity.ok(MapperUtil.getSuccessBaseResponse(cartService.getCarts()));
    }

    @GetMapping("/carts/{cartId}")
    public ResponseEntity<BaseResponseDto<CartDto>> getCartById(@PathVariable Long cartId) throws CartNotFoundException {
        return ResponseEntity.ok(MapperUtil.getSuccessBaseResponse(cartService.getCartItemsById(cartId)));
    }

    @PostMapping(value = "/carts", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponseDto<CartDto>> addItemToCart(@RequestBody @Valid ItemDto cartItemRequest) throws CartNotFoundException {
        CartDto cartDto = cartService.addItemToCart(cartItemRequest.getCartId(), cartItemRequest);
        BaseResponseDto<CartDto> successBaseResponse = MapperUtil.getSuccessBaseResponse(cartDto);
        return ResponseEntity.ok(successBaseResponse);
    }

    @PutMapping("/carts")
    public ResponseEntity<BaseResponseDto<CartDto>> updateItemToCart(@RequestBody @Valid ItemDto cartItemRequest) throws CartNotFoundException, ItemNotFoundException {
        return ResponseEntity.ok(MapperUtil.getSuccessBaseResponse(cartService.updateItemToCart(cartItemRequest.getCartId(), cartItemRequest)));
    }

    @DeleteMapping("/carts/{cartId}/item/{itemId}")
    public ResponseEntity<BaseResponseDto<CartDto>> deleteItemFromCartById(@PathVariable Long cartId, @PathVariable Long itemId)
            throws CartNotFoundException, ItemNotFoundException {
        return ResponseEntity.ok(MapperUtil.getSuccessBaseResponse(cartService.removeItemToCart(cartId, itemId)));
    }

    @DeleteMapping("/carts/{cartId}")
    public ResponseEntity<BaseResponseDto> deleteCartById(@PathVariable Long cartId) throws CartNotFoundException {
        cartService.removeCartById(cartId);
        return ResponseEntity.ok(new BaseResponseDto<>(StatusEnum.SUCCESS.getStatus(), null));
    }

}
