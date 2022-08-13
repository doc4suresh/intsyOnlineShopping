package com.intsy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intsy.entity.Category;
import com.intsy.entity.Subcategory;
import com.intsy.repository.CategoryRepository;
import com.intsy.repository.SubcategoryRepository;
import com.intsy.service.SubcategoryService;



@Service
public class SubcategoryServiceImpl implements SubcategoryService {

	@Autowired
	private SubcategoryRepository subcategoryRepository; 
	
	@Autowired
	private CategoryRepository catRepository;
	
	@Override
	public List<Subcategory> findAll() {
		return subcategoryRepository.findAll();
	}

	@Override
	public Subcategory Save(Subcategory subcategory) {
		return subcategoryRepository.save(subcategory);
	}

	@Override
	public List<Subcategory> findSubcategoriesByCatId(long catId) {
		Category category = catRepository.getOne(catId);
		return category.getSubcategotyList();
	}

	
}
