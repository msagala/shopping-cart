package com.factura.shoppingcart.service;

import com.factura.shoppingcart.exception.CartNotFoundException;
import com.factura.shoppingcart.exception.ItemNotFoundException;
import com.factura.shoppingcart.model.dto.CartDto;
import com.factura.shoppingcart.model.dto.ItemDto;
import com.factura.shoppingcart.model.entity.CartEntity;
import com.factura.shoppingcart.model.entity.ItemEntity;
import com.factura.shoppingcart.repository.CartRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
public class CartServiceTest {

    @MockBean
    private CartRepository cartRepository;
    @Autowired
    private CartService cartService;

    @Test
    public void findByIdShouldReturnResult() throws CartNotFoundException {
        CartEntity mockCartEntity = new CartEntity(1L,new HashSet<>(), BigDecimal.ZERO);
        Mockito.when(cartRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(mockCartEntity));
        CartDto cartDto = cartService.getCartItemsById(2L);
        Assertions.assertNotNull(cartDto);
    }

    @Test
    public void getByIdShouldThrowCartNotFoundException() throws CartNotFoundException {
        Mockito.when(cartRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(null));
        Assertions.assertThrows(CartNotFoundException.class,() -> cartService.getCartItemsById(2L));
    }

    @Test
    public void checkLengthOfItems() throws CartNotFoundException {
        CartEntity mockCartEntity = new CartEntity();
        mockCartEntity.setId(1L);
        Set<ItemEntity> itemEntitySet = new HashSet<>();
        itemEntitySet.add(new ItemEntity(1L,"P1002","IPhone","Cellphone",1,new BigDecimal("300.0"),null));
        itemEntitySet.add(new ItemEntity(2L,"P1003","Samsung","Cellphone",1,new BigDecimal("200.0"),null));
        itemEntitySet.add(new ItemEntity(3L,"P1004","Nokia","Cellphone",1,new BigDecimal("100.0"), null));
        mockCartEntity.setItems(itemEntitySet);
        Mockito.when(cartRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(mockCartEntity));
        Assertions.assertEquals(3, cartService.getCartItemsById(1L).getItems().size());
    }

    @Test
    public void addItemWithoutCartShouldCreateCart() throws CartNotFoundException {
        CartEntity mockCartEntity = new CartEntity();
        mockCartEntity.setId(1L);
        Set<ItemEntity> itemEntitySet = new HashSet<>();
        itemEntitySet.add(new ItemEntity(1L,"P1002","IPhone","Cellphone",1,new BigDecimal("300.0"),null));
        mockCartEntity.setItems(itemEntitySet);
        Mockito.when(cartRepository.save(Mockito.any()))
                .thenReturn(mockCartEntity);
        CartDto cartDto = cartService.addItemToCart(null, new ItemDto(1L,"P1002","IPhone","Cellphone",1,new BigDecimal("300.0")));
        Assertions.assertEquals(cartDto.getId(),1L);
        Assertions.assertEquals(cartDto.getItems().size(),1);
    }

    @Test
    public void updateItemInCart() throws CartNotFoundException, ItemNotFoundException {
        CartEntity mockCartEntity = new CartEntity();
        mockCartEntity.setId(1L);
        Set<ItemEntity> itemEntitySet = new HashSet<>();
        itemEntitySet.add(new ItemEntity(1L,"P1002","IPhone","Cellphone",1,new BigDecimal("300.0"),new BigDecimal("300.0")));
        mockCartEntity.setItems(itemEntitySet);
        mockCartEntity.setTotalPrice(new BigDecimal("300.0"));
        Mockito.when(cartRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(mockCartEntity));
        Mockito.when(cartRepository.save(Mockito.any()))
                .thenReturn(mockCartEntity);
        CartDto cartDto = cartService.updateItemToCart(1L, new ItemDto(1L,"P1002","IPhone","Cellphone",1,new BigDecimal("300.0")));
        Assertions.assertEquals(cartDto.getId(),1L);
        Assertions.assertEquals(cartDto.getItems().size(),1);
    }

    @Test
    public void deleteItemInCart() throws CartNotFoundException, ItemNotFoundException {
        CartEntity mockCartEntity = new CartEntity();
        mockCartEntity.setId(1L);
        Set<ItemEntity> itemEntitySet = new HashSet<>();
        itemEntitySet.add(new ItemEntity(1L,"P1002","IPhone","Cellphone",1,new BigDecimal("300.0"),new BigDecimal("300.0")));
        mockCartEntity.setItems(itemEntitySet);
        mockCartEntity.setTotalPrice(new BigDecimal("300.0"));
        Mockito.when(cartRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(mockCartEntity));
        Mockito.when(cartRepository.save(Mockito.any()))
                .thenReturn(mockCartEntity);
        CartDto cartDto = cartService.removeItemToCart(1L, 1L);
        Assertions.assertEquals(cartDto.getId(),1L);
        Assertions.assertEquals(cartDto.getItems().size(),0L);
    }

}
