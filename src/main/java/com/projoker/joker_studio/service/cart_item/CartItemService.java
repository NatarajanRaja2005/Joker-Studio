package com.projoker.joker_studio.service.cart_item;

import com.projoker.joker_studio.exception.AlreadyExistException;
import com.projoker.joker_studio.exception.ItemNotExistException;
import com.projoker.joker_studio.model.Cart;
import com.projoker.joker_studio.model.CartItem;
import com.projoker.joker_studio.model.Product;
import com.projoker.joker_studio.repository.CartItemRepository;
import com.projoker.joker_studio.repository.CartRepository;
import com.projoker.joker_studio.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService{
    public final CartItemRepository cartItemRepository;
    public final IProductService productService;
    public final CartRepository cartRepository;

    @Override
    public CartItem createCartItem(Long productId, int quantity,Cart cart){
        CartItem item=getCartItemByProductIdAndCartId(productId,cart.getId());
        if(item!=null){
            return updateCartItem(productId,quantity,cart.getId());
        }
        //If product not found it will throw exception on product service method
        Product product=productService.getProductById(productId);
        item=new CartItem();
        item.setProduct(product);
        item.setUnitPrice(product.getPrice());
        item.setQuantity(quantity);
        item.UpdateTotalPrice();
        item.setCart(cart);
        cartItemRepository.save(item);
        return item;
    }

    @Override
    public CartItem updateCartItem(Long productId, int quantity,Long cartId) {
        CartItem item=getCartItemByProductIdAndCartId(productId,cartId);
        if(item==null){
            throw new ItemNotExistException("Product not exists");
        }
        //Caution: After this update you should have to perform updatation og total amount
        //You done that on your controller because if you done here may create an loop
        item.setQuantity(quantity);
        item.UpdateTotalPrice();
        return cartItemRepository.save(item);
    }

    @Override
    public void deleteCartItemByProductId(Long productId,Long cartId) {
        CartItem item=getCartItemByProductIdAndCartId(productId,cartId);
        if(item==null){
            throw new ItemNotExistException("Product not exists");
        }
        cartItemRepository.deleteById(item.getId());
        //Same thing Here you Should have to make updation of your cart. Do it on controller
    }

    @Override
    public CartItem getCartItemByProductIdAndCartId(Long productId,Long cartId) {
        Product product=productService.getProductById(productId);
        if(product==null){
            throw new ItemNotExistException("Product not exists");
        }
        Optional<Cart> cart=cartRepository.findById(cartId);
        if(cart==null){
            throw new ItemNotExistException("Cart not exists");
        }
        CartItem item=cartItemRepository.findByCartAndProduct(cart.get(),product);
        return item;
    }
}
