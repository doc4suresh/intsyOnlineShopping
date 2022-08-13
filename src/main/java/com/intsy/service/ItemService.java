package com.intsy.service;

import java.util.List;

import com.intsy.entity.Category;
import com.intsy.entity.Item;
import com.intsy.entity.Subcategory;






public interface ItemService {
	
	Item save(Item item);
		
	Item findOne(Long id);

	List<Item> findAll();

	void update(Item item);
	
	List<Item> findAllActive();

	List<Item> findByCategory(Category category);
	
	List<Item> findBySubcategory(Subcategory subcategory);

	List<Item> findBySpecials();

	List<Item> findByNewArrivals();

	

}
