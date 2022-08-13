package com.intsy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intsy.entity.Item;





public interface ItemRepository extends JpaRepository<Item, Long> {


	
	

}
