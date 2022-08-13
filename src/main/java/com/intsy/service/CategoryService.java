package com.intsy.service;

import java.util.List;

import com.intsy.entity.Category;



public interface CategoryService {
	
	List<Category> findAll();
	
	Category save(Category category);

}
