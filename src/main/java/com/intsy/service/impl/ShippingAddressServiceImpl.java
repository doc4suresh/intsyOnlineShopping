package com.intsy.service.impl;

import org.springframework.stereotype.Service;

import com.intsy.entity.ShippingAddress;
import com.intsy.entity.UserShipping;
import com.intsy.service.ShippingAddressService;

@Service
public class ShippingAddressServiceImpl implements ShippingAddressService {
	public ShippingAddress setByUserShipping(UserShipping userShipping, ShippingAddress shippingAddress) {
		shippingAddress.setShippingAddressName(userShipping.getUserShippingName());
		shippingAddress.setShippingAddressStreet1(userShipping.getUserShippingStreet1());
		shippingAddress.setShippingAddressStreet2(userShipping.getUserShippingStreet2());
		shippingAddress.setShippingAddressCity(userShipping.getUserShippingCity());
		shippingAddress.setShippingAddressDistrict(userShipping.getUserShippingDistrict());
		shippingAddress.setShippingAddressPostalCode(userShipping.getUserShippingPostalcode());
		
		return shippingAddress;
	}
}
