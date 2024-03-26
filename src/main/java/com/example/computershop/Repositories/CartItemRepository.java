package com.example.computershop.Repositories;

import com.example.computershop.Entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    CartItem findCartItemByProductProductIdAndCartCartId(Long cartId, Long productId);
}
