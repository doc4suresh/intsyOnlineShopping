package com.intsy.service.impl;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intsy.entity.CartItem;
import com.intsy.entity.Item;
import com.intsy.entity.ItemToCartItem;
import com.intsy.entity.Order;
import com.intsy.entity.ShoppingCart;
import com.intsy.entity.User;
import com.intsy.repository.CartItemRepository;
import com.intsy.repository.ItemToCartItemRepository;
import com.intsy.service.CartItemService;

@Service
public class CartItemServiceImpl implements CartItemService {

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private ItemToCartItemRepository itemToCartItemRepository;

	public List<CartItem> findByShoppingCart(ShoppingCart shoppingCart) {
		return cartItemRepository.findByShoppingCart(shoppingCart);
	}

	public CartItem updateCartItem(CartItem cartItem, User user) {
		BigDecimal bigDecimal;
		bigDecimal = new BigDecimal(cartItem.getItem().getDiscountedPrice(user)).multiply(new BigDecimal(cartItem.getQty()));

		bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
		cartItem.setSubtotal(bigDecimal);

		cartItemRepository.save(cartItem);

		return cartItem;
	}

	public CartItem addItemToCartItem(Item item, User user, int qty) {
		List<CartItem> cartItemList = findByShoppingCart(user.getShoppingCart());

		for (CartItem cartItem : cartItemList) {
			if (item.getItemId() == cartItem.getItem().getItemId()) {
				cartItem.setQty(cartItem.getQty() + qty);
				cartItem.setSubtotal(new BigDecimal(item.getDiscountedPrice(user)).multiply(new BigDecimal(qty)));
				cartItemRepository.save(cartItem);
				return cartItem;
			}
		}

		CartItem cartItem = new CartItem();
		cartItem.setShoppingCart(user.getShoppingCart());
		cartItem.setItem(item);

		cartItem.setQty(qty);
		cartItem.setSubtotal(new BigDecimal(item.getSellPrice()).multiply(new BigDecimal(qty)));
		cartItem = cartItemRepository.save(cartItem);

		ItemToCartItem itemToCartItem = new ItemToCartItem();
		itemToCartItem.setItem(item);
		itemToCartItem.setCartItem(cartItem);
		itemToCartItemRepository.save(itemToCartItem);

		return cartItem;
	}

	public CartItem findById(Long id) {
		return cartItemRepository.getOne(id);
	}

	public void removeFromCartItem(CartItem cartItem) {
		itemToCartItemRepository.deleteByCartItem(cartItem);
		cartItemRepository.delete(cartItem);
	}

	public CartItem save(CartItem cartItem) {
		return cartItemRepository.save(cartItem);
	}

	public List<CartItem> findByOrder(Order order) {
		return cartItemRepository.findByOrder(order);
	}



}
