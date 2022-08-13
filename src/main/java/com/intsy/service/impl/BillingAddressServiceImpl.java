package com.intsy.service.impl;

import org.springframework.stereotype.Service;

import com.intsy.entity.BillingAddress;
import com.intsy.entity.UserBilling;
import com.intsy.service.BillingAddressService;

@Service
public class BillingAddressServiceImpl implements BillingAddressService{
	
	public BillingAddress setByUserBilling(UserBilling userBilling, BillingAddress billingAddress) {
		billingAddress.setBillingAddressName(userBilling.getUserBillingName());
		billingAddress.setBillingAddressStreet1(userBilling.getUserBillingStreet1());
		billingAddress.setBillingAddressStreet2(userBilling.getUserBillingStreet2());
		billingAddress.setBillingAddressCity(userBilling.getUserBillingCity());
		billingAddress.setBillingAddressDistrict(userBilling.getUserBillingDistrict());
		billingAddress.setBillingAddressPostalCode(userBilling.getUserBillingPostalCode());
		
		return billingAddress;
	}

}
