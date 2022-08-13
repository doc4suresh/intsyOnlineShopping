package com.intsy.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intsy.entity.UserPayment;
import com.intsy.repository.UserPaymentRepository;
import com.intsy.service.UserPaymentService;

@Service
public class UserPaymentServiceImpl implements UserPaymentService{

	@Autowired
	private UserPaymentRepository userPaymentRepository;
		
	public UserPayment findById(Long id) {
		return userPaymentRepository.getOne(id);
	}
	
	public void removeById(Long id) {
		userPaymentRepository.deleteById(id);
	}
} 
