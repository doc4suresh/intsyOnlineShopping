package com.intsy.service;

import java.util.List;

import com.intsy.entity.Brand;



public interface BrandService {

	List<Brand> findAll();
	
	Brand save(Brand brand);
	
	public Void delete(Brand brand);

}
