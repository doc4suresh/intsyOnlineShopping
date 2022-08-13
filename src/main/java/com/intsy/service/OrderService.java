package com.intsy.service;

import com.intsy.entity.BillingAddress;
import com.intsy.entity.Order;
import com.intsy.entity.Payment;
import com.intsy.entity.ShippingAddress;
import com.intsy.entity.ShoppingCart;
import com.intsy.entity.User;

public interface OrderService {
	Order createOrder(ShoppingCart shoppingCart,
			ShippingAddress shippingAddress,
			BillingAddress billingAddress,
			Payment payment,
			String shippingMethod,
			User user);
	
	Order findOne(Long id);
}
