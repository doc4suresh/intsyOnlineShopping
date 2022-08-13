package com.intsy.service;

import java.util.List;

import com.intsy.entity.security.Role;



public interface RoleService {
	
	List<Role> findAll();
	
	Role findByName(String name) ;
}

