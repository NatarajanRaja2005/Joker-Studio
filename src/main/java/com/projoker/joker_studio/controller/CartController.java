package com.projoker.joker_studio.controller;

import com.projoker.joker_studio.dto.CartDto;
import com.projoker.joker_studio.dto.CartItemDto;
import com.projoker.joker_studio.model.Cart;
import com.projoker.joker_studio.model.CartItem;
import com.projoker.joker_studio.response.ApiResponse;
import com.projoker.joker_studio.service.cart.ICartService;
import com.projoker.joker_studio.service.cart_item.ICartItemService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/cart")
public class CartController {
    private final ICartService cartService;
    private final ModelMapper modelMapper;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestParam Long cartId,
                                                     @RequestParam Long productId,
                                                     @RequestParam int quantity){
        try {
            cartService.addItemsToCart(cartId,productId,quantity);
            return ResponseEntity.ok(new ApiResponse("Item added to the Cart :)",null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Add item is Failed",e.getMessage()));
        }
    }

    @DeleteMapping("/remove/")
    public ResponseEntity<ApiResponse> removeItemFromCart(@RequestParam Long cartId,
                                                          @RequestParam Long productId){
        try {
            cartService.removeItemFromCart(cartId,productId);
            return ResponseEntity.ok(new ApiResponse("Item Removed from the cart Successfully.",null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Delete Item is Failed!",e.getMessage()));
        }
    }

    @GetMapping("/get/items/{cartId}")
    public ResponseEntity<ApiResponse> getAllCartItem(@PathVariable Long cartId){
        try {
            Set<CartItem> set=cartService.getAllItemsInCart(cartId);
            List<CartItemDto> setDto=set
                    .stream()
                    .map(item-> modelMapper.map(item,CartItemDto.class))
                    .toList();

            return ResponseEntity.ok(new ApiResponse("All cart items are retrived.",setDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Get all Item from cart is Failed!",e.getMessage()));
        }
    }

    @GetMapping("/get/{cartId}")
    public ResponseEntity<ApiResponse> getCartById(@PathVariable Long cartId){
        try {
            Cart cart=cartService.getCartById(cartId);
            CartDto cartDto=modelMapper.map(cart,CartDto.class);
            return ResponseEntity.ok(new ApiResponse("Cart is retrived Successfully.",cartDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Getting cart is Failed!",e.getMessage()));
        }
    }

    @PutMapping("/clear/{cartId}")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId){
        try {
            cartService.clearCart(cartId);
            return ResponseEntity.ok(new ApiResponse("Cart Cleared Successfully.",null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Clearing Cart is Failed!",e.getMessage()));
        }
    }
}
