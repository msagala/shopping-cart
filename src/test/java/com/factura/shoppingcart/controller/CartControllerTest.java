package com.factura.shoppingcart.controller;

import com.factura.shoppingcart.config.JsonConfiguration;
import com.factura.shoppingcart.config.SecurityConfig;
import com.factura.shoppingcart.constant.StatusEnum;
import com.factura.shoppingcart.model.dto.CartDto;
import com.factura.shoppingcart.model.dto.ItemDto;
import com.factura.shoppingcart.model.dto.base.BaseResponseDto;
import com.factura.shoppingcart.model.entity.ItemEntity;
import com.factura.shoppingcart.service.CartService;
import com.factura.shoppingcart.util.MapperUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
@Import(value = { JsonConfiguration.class, SecurityConfig.class})
@AutoConfigureRestDocs
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CartService cartService;

    private final String basicAuth = "Basic dXNlcjpQQHNzdzByZA==";

    @Test
    public void testGetCartByIdSuccess() throws Exception {
        BaseResponseDto<CartDto> cartDtoBaseResponseDto = new BaseResponseDto<>();
        cartDtoBaseResponseDto.setStatus(StatusEnum.SUCCESS.getStatus());
        CartDto cartDto = new CartDto();
        cartDto.setId(1L);
        Set<ItemEntity> itemEntitySet = new HashSet<>();
        itemEntitySet.add(new ItemEntity(1L,"P1002","IPhone","Cellphone",1,new BigDecimal("300.0"),null));
        itemEntitySet.add(new ItemEntity(2L,"P1003","Samsung","Cellphone",1,new BigDecimal("200.0"),null));
        itemEntitySet.add(new ItemEntity(3L,"P1004","Nokia","Cellphone",1,new BigDecimal("100.0"), null));
        cartDto.setItems(itemEntitySet);
        cartDto.setTotalPrice(new BigDecimal("600.0"));
        cartDtoBaseResponseDto.setData(cartDto);
        Mockito.when(cartService.getCartItemsById(Mockito.anyLong()))
                .thenReturn(cartDto);
        this.mockMvc.perform(get("/api/shopping-cart/carts/{id}", 1L)
                        .header("Authorization",basicAuth))
                .andDo(document( "get-cart-id",
                        Preprocessors.preprocessRequest( Preprocessors.prettyPrint() ),
                        Preprocessors.preprocessResponse( Preprocessors.prettyPrint() )))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddItemsInNewCartSuccess() throws Exception {

        ItemDto itemDto = new ItemDto();
        itemDto.setCartId(null);
        itemDto.setItemCode("P10020");
        itemDto.setItemCategory("Cellphone");
        itemDto.setItemName("IPhone");
        itemDto.setItemPrice(new BigDecimal("5000.0"));
        itemDto.setItemQuantity(2);

        CartDto cartDto = new CartDto();
        cartDto.setId(1L);
        Set<ItemEntity> itemEntitySet = new HashSet<>();
        itemEntitySet.add(new ItemEntity(1L,"P10020","IPhone","Cellphone",2,new BigDecimal("5000.0"),new BigDecimal("10000.0")));
        cartDto.setTotalPrice(new BigDecimal("10000.0"));
        cartDto.setItems(itemEntitySet);

        Mockito.when(cartService.addItemToCart(Mockito.any(), Mockito.any()))
                .thenReturn(cartDto);

        this.mockMvc.perform(post("/api/shopping-cart/carts")
                        .header("Authorization",basicAuth)
                        .content(MapperUtil.objectToJson(itemDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(document( "add-item-in-new-cart",
                        Preprocessors.preprocessRequest( Preprocessors.prettyPrint() ),
                        Preprocessors.preprocessResponse( Preprocessors.prettyPrint() )))
                .andExpect(status().isOk());

    }

    @Test
    public void testAddItemsInExistingCartSuccess() throws Exception {

        ItemDto itemDto = new ItemDto();
        itemDto.setCartId(1L);
        itemDto.setItemCode("P10021");
        itemDto.setItemCategory("Cellphone");
        itemDto.setItemName("Samsung");
        itemDto.setItemPrice(new BigDecimal("10000.0"));
        itemDto.setItemQuantity(1);

        CartDto cartDto = new CartDto();
        cartDto.setId(1L);
        Set<ItemEntity> itemEntitySet = new HashSet<>();
        itemEntitySet.add(new ItemEntity(1L,"P10020","IPhone","Cellphone",2,new BigDecimal("5000.0"),new BigDecimal("10000.0")));
        itemEntitySet.add(new ItemEntity(2L,"P10021","Samsung","Cellphone",1,new BigDecimal("10000.0"),new BigDecimal("10000.0")));
        cartDto.setTotalPrice(new BigDecimal("20000.0"));
        cartDto.setItems(itemEntitySet);

        Mockito.when(cartService.addItemToCart(Mockito.any(), Mockito.any()))
                .thenReturn(cartDto);

        this.mockMvc.perform(post("/api/shopping-cart/carts")
                        .header("Authorization",basicAuth)
                        .content(MapperUtil.objectToJson(itemDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(document( "add-item-in-existing-cart",
                        Preprocessors.preprocessRequest( Preprocessors.prettyPrint() ),
                        Preprocessors.preprocessResponse( Preprocessors.prettyPrint() )))
                .andExpect(status().isOk());

    }

    @Test
    public void testUpdateItemInCartSuccess() throws Exception {

        ItemDto itemDto = new ItemDto();
        itemDto.setCartId(1L);
        itemDto.setItemCode("P10021");
        itemDto.setItemCategory("Cellphone");
        itemDto.setItemName("Nokia");
        itemDto.setItemPrice(new BigDecimal("3000.0"));
        itemDto.setItemQuantity(2);

        CartDto cartDto = new CartDto();
        cartDto.setId(1L);
        Set<ItemEntity> itemEntitySet = new HashSet<>();
        itemEntitySet.add(new ItemEntity(1L,"P10020","IPhone","Cellphone",2,new BigDecimal("5000.0"),new BigDecimal("10000.0")));
        itemEntitySet.add(new ItemEntity(2L,"P10021","Nokia","Cellphone",2,new BigDecimal("3000.0"),new BigDecimal("6000.0")));
        cartDto.setTotalPrice(new BigDecimal("16000.0"));
        cartDto.setItems(itemEntitySet);

        Mockito.when(cartService.updateItemToCart(Mockito.any(), Mockito.any()))
                .thenReturn(cartDto);

        this.mockMvc.perform(put("/api/shopping-cart/carts")
                        .header("Authorization",basicAuth)
                        .content(MapperUtil.objectToJson(itemDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(document( "update-item-in-cart",
                        Preprocessors.preprocessRequest( Preprocessors.prettyPrint() ),
                        Preprocessors.preprocessResponse( Preprocessors.prettyPrint() )))
                .andExpect(status().isOk());

    }

    @Test
    public void testRemoveItemInCartSuccess() throws Exception {

        CartDto cartDto = new CartDto();
        cartDto.setId(1L);
        Set<ItemEntity> itemEntitySet = new HashSet<>();
        itemEntitySet.add(new ItemEntity(1L,"P10020","IPhone","Cellphone",2,new BigDecimal("5000.0"),new BigDecimal("10000.0")));
        cartDto.setTotalPrice(new BigDecimal("10000.0"));
        cartDto.setItems(itemEntitySet);

        Mockito.when(cartService.removeItemToCart(Mockito.any(), Mockito.any()))
                .thenReturn(cartDto);

        this.mockMvc.perform(delete("/api/shopping-cart/carts/{cartId}/item/{itemId}", 1L, 2L)
                        .header("Authorization",basicAuth)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(document( "delete-item-in-cart",
                        Preprocessors.preprocessRequest( Preprocessors.prettyPrint() ),
                        Preprocessors.preprocessResponse( Preprocessors.prettyPrint() )))
                .andExpect(status().isOk());


    }



}
