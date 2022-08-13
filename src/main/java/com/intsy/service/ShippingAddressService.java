package com.intsy.service;

import com.intsy.entity.ShippingAddress;
import com.intsy.entity.UserShipping;

public interface ShippingAddressService {
	ShippingAddress setByUserShipping(UserShipping userShipping, ShippingAddress shippingAddress);
}
