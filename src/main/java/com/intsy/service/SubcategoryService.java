package com.intsy.service;

import java.util.List;

import com.intsy.entity.Subcategory;



public interface SubcategoryService {
	
	List<Subcategory> findAll();
	
	List<Subcategory> findSubcategoriesByCatId(long catId);
	
	Subcategory Save(Subcategory subcategory);

}
