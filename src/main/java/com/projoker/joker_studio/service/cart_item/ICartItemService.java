package com.projoker.joker_studio.service.cart_item;

import com.projoker.joker_studio.model.Cart;
import com.projoker.joker_studio.model.CartItem;

public interface ICartItemService {
    CartItem createCartItem(Long productId, int quantity, Cart cart);
    CartItem updateCartItem(Long productId,int quantity,Long cartId);
    void deleteCartItemByProductId(Long productId,Long cartId);
    CartItem getCartItemByProductIdAndCartId(Long productId,Long cartId);
}
