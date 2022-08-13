package com.intsy.service;

import com.intsy.entity.Payment;
import com.intsy.entity.UserPayment;

public interface PaymentService {
	Payment setByUserPayment(UserPayment userPayment, Payment payment);
}
