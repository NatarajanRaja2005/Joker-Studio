package com.projoker.joker_studio.service.cart;

import com.projoker.joker_studio.dto.CartDto;
import com.projoker.joker_studio.exception.ItemNotExistException;
import com.projoker.joker_studio.model.Cart;
import com.projoker.joker_studio.model.CartItem;
import com.projoker.joker_studio.model.Product;
import com.projoker.joker_studio.model.User;
import com.projoker.joker_studio.repository.CartRepository;
import com.projoker.joker_studio.repository.UserRepository;
import com.projoker.joker_studio.service.cart_item.ICartItemService;
import com.projoker.joker_studio.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService{
    public final CartRepository cartRepository;
    public final ICartItemService cartItemService;
    public final UserRepository userRepository;

    @Override
    public void createCart(Long userId) {
        Optional<User> existUser=userRepository.findById(userId);
        Cart newCart=new Cart();
        newCart.setUser(existUser.get());
        existUser.get().setCart(newCart);
        userRepository.save(existUser.get());
    }

    @Override
    public void addItemsToCart(Long cartId, Long productId, int quantity) {
        Cart cart=getCartById(cartId);
        if(cart==null){
            throw new ItemNotExistException("Invalid Cart Id");
        }
        CartItem item=cartItemService.createCartItem(productId,quantity,cart);
        cart.getItemList().add(item);
        updatePriceOfCart(cartId);
    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart=getCartById(cartId);
        CartItem item=cartItemService.getCartItemByProductIdAndCartId(productId,cartId);
        cart.getItemList().remove(item);
        updatePriceOfCart(cartId);
    }

    @Override
    public Set<CartItem> getAllItemsInCart(Long cartId) {
        Cart cart=getCartById(cartId);
        return cart.getItemList();
    }

    @Override
    public Cart getCartById(Long cartId) {
        Optional<Cart> cart=cartRepository.findById(cartId);
        if(cart.isEmpty()) {
            throw new ItemNotExistException("Cart is Not Found");
        }
        return cart.get();
    }

    @Override
    public void updatePriceOfCart(Long cartId) {
        Cart cart=getCartById(cartId);
        BigDecimal price= cart.getItemList()
                .stream()
                .map(item->
                   item.getTotalPrice()==null?BigDecimal.ZERO:item.getTotalPrice())
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        cart.setTotalAmount(price);
        cartRepository.save(cart);
    }

    @Override
    public void clearCart(Long cartId) {
        Cart cart=getCartById(cartId);
        cart.setTotalAmount(BigDecimal.ZERO);
        cart.getItemList().clear();
        cartRepository.save(cart);
    }

}
