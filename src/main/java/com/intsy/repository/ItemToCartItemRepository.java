package com.intsy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.intsy.entity.CartItem;
import com.intsy.entity.ItemToCartItem;

@Transactional
public interface ItemToCartItemRepository extends JpaRepository<ItemToCartItem, Long> {
	void deleteByCartItem(CartItem cartItem);
}
