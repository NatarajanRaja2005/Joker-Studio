package com.projoker.joker_studio.repository;

import com.projoker.joker_studio.model.Cart;
import com.projoker.joker_studio.model.CartItem;
import com.projoker.joker_studio.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    CartItem findByCartAndProduct(Cart cart, Product product);
}
