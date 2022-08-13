package com.intsy.repository;

import org.springframework.data.repository.CrudRepository;

import com.intsy.entity.security.Role;

public interface RoleRepository extends CrudRepository<Role,  Long>{
	
	Role findByName(String name);
}
