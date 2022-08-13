package com.intsy.service;

import java.security.Principal;

import com.intsy.entity.ShoppingCart;
import com.intsy.entity.User;

public interface ShoppingCartService {
	ShoppingCart updateShoppingCart(ShoppingCart shoppingCart, User user);
	
	void clearShoppingCart(ShoppingCart shoppingCart);
}
