package com.intsy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intsy.entity.UserShipping;
import com.intsy.repository.UserShippingRepository;
import com.intsy.service.UserShippingService;

@Service
public class UserShippingServiceImpl implements UserShippingService{
	
	@Autowired
	private UserShippingRepository userShippingRepository;
	
	
	public UserShipping findById(Long id) {
		return userShippingRepository.getOne(id);
	}
	
	public void removeById(Long id) {
		userShippingRepository.deleteById(id);;
	}

}
