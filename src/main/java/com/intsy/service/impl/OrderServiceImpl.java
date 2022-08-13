package com.intsy.service.impl;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intsy.entity.BillingAddress;
import com.intsy.entity.CartItem;
import com.intsy.entity.Item;
import com.intsy.entity.Order;
import com.intsy.entity.Payment;
import com.intsy.entity.ShippingAddress;
import com.intsy.entity.ShoppingCart;
import com.intsy.entity.User;
import com.intsy.repository.OrderRepository;
import com.intsy.service.CartItemService;
import com.intsy.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CartItemService cartItemService;
	
	public synchronized Order createOrder(ShoppingCart shoppingCart,
			ShippingAddress shippingAddress,
			BillingAddress billingAddress,
			Payment payment,
			String shippingMethod,
			User user) {
		Order order = new Order();
		order.setBillingAddress(billingAddress);
		order.setOrderStatus("created");
		order.setPayment(payment);
		order.setShippingAddress(shippingAddress);
		order.setShippingMethod(shippingMethod);
		
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		
		for(CartItem cartItem : cartItemList) {
			Item item = cartItem.getItem();
			cartItem.setOrder(order);
			item.setQty(item.getQty()  - cartItem.getQty());
		}
		
		order.setCartItemList(cartItemList);
		order.setOrderDate(LocalDate.now());
		order.setOrderTotal(shoppingCart.getGrandTotal());
		shippingAddress.setOrder(order);
		billingAddress.setOrder(order);
		payment.setOrder(order);
		order.setUser(user);
		order = orderRepository.save(order);
		
		return order;
	}
	
	public Order findOne(Long id) {
		return orderRepository.getOne(id);
	}

}
