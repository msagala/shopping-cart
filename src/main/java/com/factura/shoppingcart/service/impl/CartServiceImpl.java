package com.factura.shoppingcart.service.impl;

import com.factura.shoppingcart.exception.CartNotFoundException;
import com.factura.shoppingcart.exception.ItemNotFoundException;
import com.factura.shoppingcart.model.dto.CartDto;
import com.factura.shoppingcart.model.dto.ItemDto;
import com.factura.shoppingcart.model.entity.CartEntity;
import com.factura.shoppingcart.model.entity.ItemEntity;
import com.factura.shoppingcart.repository.CartRepository;
import com.factura.shoppingcart.service.CartService;
import com.factura.shoppingcart.util.MapperUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@Log4j2
public class CartServiceImpl implements CartService {
    private CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public CartDto addItemToCart(Long cartId, ItemDto itemDto) throws CartNotFoundException {
        log.info("Adding item to cart Id number: {}", cartId);
        log.info("ItemDTO: {}", MapperUtil.objectToJson(itemDto));
        ItemEntity item = mapCartDtoToItemEntity(itemDto);
        CartEntity cartEntity;
        if(cartId != null) {
            log.info("Creating new cart because cart Id is null");
            Optional<CartEntity> cartEntityOptional = cartRepository.findById(cartId);
            if (!cartEntityOptional.isPresent()) {
                // If no cart found, throw exception cart not
                throw getCartNotFoundException();
            } else {
                // If cart found, add the new item on the cart
                // If item is existing, just add  the quantity
                cartEntity = cartEntityOptional.get();
                AtomicBoolean isItemExisting = new AtomicBoolean(false);
                cartEntity.getItems().stream()
                        .filter(itemStream -> itemStream.getItemCode().equals(item.getItemCode()))
                        .forEach(itemStream -> {
                            itemStream.setQuantity(itemStream.getQuantity() + item.getQuantity());
                            itemStream.setTotalPrice(itemStream.getPrice().multiply(new BigDecimal(itemStream.getQuantity())));
                            isItemExisting.set(true);
                            log.info("Updated the quantity of the item in the cart!");
                        });
                // If item is not existing, add the new item
                if (!isItemExisting.get()) {
                    item.setTotalPrice(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
                    cartEntity.getItems().add(item);
                    log.info("Added new item in the cart!");
                }
                cartEntity.setTotalPrice(getTotalAmountByCart(cartEntity));
                cartEntity = cartRepository.save(cartEntity);
            }
        } else {
            // If no cart id, create new cart to use
            CartEntity newCart = new CartEntity();
            item.setTotalPrice(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
            newCart.getItems().add(item);
            newCart.setTotalPrice(item.getTotalPrice());
            cartEntity = cartRepository.save(newCart);
            log.info("Created new cart and added first item!");
        }
        CartDto cartDto = mapCartEntityToCartDTO(cartEntity);
        log.info("Cart Items now: {}", MapperUtil.objectToJson(cartDto));
        return cartDto;
    }

    @Override
    public CartDto removeItemToCart(Long cartId, Long itemId) throws CartNotFoundException, ItemNotFoundException {
        log.info("Removing item number {} to cart Id number {}",itemId, cartId);
        Optional<CartEntity> cartEntityOptional = cartRepository.findById(cartId);
        if(cartEntityOptional.isPresent()) {
            CartEntity cartEntity = cartEntityOptional.get();
            ItemEntity itemEntity = cartEntity.getItems().stream().filter(item -> item.getId() == itemId)
                    .findFirst()
                    .orElseThrow(() -> new ItemNotFoundException());
            cartEntity.setTotalPrice(cartEntity.getTotalPrice().subtract(itemEntity.getTotalPrice()));
            cartEntity.getItems().removeIf(item -> item.getId() == itemId);
            CartDto cartDto = mapCartEntityToCartDTO(cartRepository.save(cartEntity));
            log.info("Cart item deleted! Remaining items in cart: {}", MapperUtil.objectToJson(cartDto));
            return mapCartEntityToCartDTO(cartRepository.save(cartEntity));
        }
        throw getCartNotFoundException();
    }

    @Override
    public CartDto updateItemToCart(Long cartId, ItemDto itemDto) throws CartNotFoundException {
        log.info("Updating item to cart Id number: {}", cartId);
        log.info("ItemDTO: {}", MapperUtil.objectToJson(itemDto));
        ItemEntity item = mapCartDtoToItemEntity(itemDto);
        Optional<CartEntity> cartEntityOptional = cartRepository.findById(cartId);
        if(cartEntityOptional.isPresent()) {
            CartEntity cartEntity = cartEntityOptional.get();
            cartEntity.getItems().stream().filter(itemStream -> itemStream.getItemCode().equals(item.getItemCode()))
                    .findAny().ifPresent(itemStream -> {
                        cartEntity.setTotalPrice(cartEntity.getTotalPrice().subtract(itemStream.getTotalPrice()));
                        itemStream.setQuantity(item.getQuantity());
                        itemStream.setCategory(item.getCategory());
                        itemStream.setName(item.getName());
                        itemStream.setPrice(item.getPrice());
                        itemStream.setTotalPrice(itemStream.getPrice()
                                .multiply(BigDecimal.valueOf(itemStream.getQuantity())));
                        cartEntity.setTotalPrice(cartEntity.getTotalPrice().add(itemStream.getTotalPrice()));
                    });
            CartDto cartDto = mapCartEntityToCartDTO(cartRepository.save(cartEntity));
            log.info("Cart updated: {}", MapperUtil.objectToJson(cartDto));
            return mapCartEntityToCartDTO(cartRepository.save(cartEntity));
        }

        throw getCartNotFoundException();
    }

    @Override
    public CartDto getCartItemsById(Long cartId) throws CartNotFoundException {
        log.info("Getting cart items to cart Id number: {}", cartId);
        CartEntity cartEntity = cartRepository.findById(cartId).orElseThrow(() -> getCartNotFoundException());
        return mapCartEntityToCartDTO(cartEntity);
    }

    @Override
    public List<CartDto> getCarts() {
        log.info("Getting all carts");
        List<CartEntity> cartEntities = cartRepository.findAll();
        List<CartDto> cartDtos = cartEntities.stream()
                .map(cartEntity -> mapCartEntityToCartDTO(cartEntity))
                .collect(Collectors.toList());
        return cartDtos;
    }

    @Override
    public void removeCartById(long cartId) {

    }

    private BigDecimal getTotalAmountByCart(CartEntity cartEntity) {
        return cartEntity.getItems().stream()
                .map(item -> item.getTotalPrice())
                .reduce(BigDecimal.ZERO, (initialValue, element) -> initialValue.add(element));
    }

    private ItemEntity mapCartDtoToItemEntity(ItemDto cartItemDTO) {
        ItemEntity item = new ItemEntity();
        item.setName(cartItemDTO.getItemName());
        item.setCategory(cartItemDTO.getItemCategory());
        item.setQuantity(cartItemDTO.getItemQuantity());
        item.setPrice(cartItemDTO.getItemPrice());
        item.setItemCode(cartItemDTO.getItemCode());
        return item;
    }

    private CartDto mapCartEntityToCartDTO(CartEntity cartEntity) {
        return new CartDto(cartEntity.getId(), cartEntity.getItems(), cartEntity.getTotalPrice());
    }

    private CartNotFoundException getCartNotFoundException() {
        CartNotFoundException cartNotFoundException = new CartNotFoundException();
        log.error(cartNotFoundException.getMessage(), cartNotFoundException);
        return cartNotFoundException;
    }

}
