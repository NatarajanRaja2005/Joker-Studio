package com.projoker.joker_studio.service.cart;

import com.projoker.joker_studio.dto.CartDto;
import com.projoker.joker_studio.model.Cart;
import com.projoker.joker_studio.model.CartItem;

import java.util.List;
import java.util.Set;

public interface ICartService {
    void createCart(Long userId);
    void addItemsToCart(Long cartId,Long productId,int quantity);
    void removeItemFromCart(Long cartId,Long productId);
    Set<CartItem> getAllItemsInCart(Long cartId);
    Cart getCartById(Long cartId);
    void updatePriceOfCart(Long cartId);
    void clearCart(Long cartId);
}
