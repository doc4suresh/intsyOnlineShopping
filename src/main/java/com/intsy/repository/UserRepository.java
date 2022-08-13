package com.intsy.repository;

import org.springframework.data.repository.CrudRepository;

import com.intsy.entity.User;

public interface UserRepository extends CrudRepository< User, Long> {
	
	User findByUsername(String username);
	
}
