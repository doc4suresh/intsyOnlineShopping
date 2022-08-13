package com.intsy.service;

import java.security.Principal;
import java.util.List;

import com.intsy.entity.CartItem;
import com.intsy.entity.Item;
import com.intsy.entity.Order;
import com.intsy.entity.ShoppingCart;
import com.intsy.entity.User;




public interface CartItemService {
	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
	
	CartItem updateCartItem(CartItem cartItem, User user);
	
	CartItem addItemToCartItem(Item item, User user, int qty);
	
	CartItem findById(Long id);
	
	void removeFromCartItem(CartItem cartItem);
	
	CartItem save(CartItem cartItem);
	
	List<CartItem> findByOrder(Order order);
}
