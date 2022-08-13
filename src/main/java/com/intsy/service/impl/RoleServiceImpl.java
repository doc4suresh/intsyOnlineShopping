package com.intsy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intsy.entity.security.Role;
import com.intsy.repository.RoleRepository;
import com.intsy.service.RoleService;



@Service
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	public List<Role> findAll(){
		
		return (List<Role>) roleRepository.findAll();
	}
	
	@Override
	public Role findByName(String name) {
		return roleRepository.findByName(name);
	}

}
