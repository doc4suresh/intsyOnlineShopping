package com.intsy.service;

import com.intsy.entity.BillingAddress;
import com.intsy.entity.UserBilling;

public interface BillingAddressService {
	BillingAddress setByUserBilling(UserBilling userBilling, BillingAddress billingAddress);
}
