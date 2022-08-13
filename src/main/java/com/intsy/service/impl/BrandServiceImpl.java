package com.intsy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intsy.entity.Brand;
import com.intsy.repository.BrandRepository;
import com.intsy.service.BrandService;


@Service
public class BrandServiceImpl implements BrandService {

	@Autowired
	private BrandRepository brandRepository;

	@Override
	public List<Brand> findAll() {
		return brandRepository.findAll();
	}

	@Override
	public Brand save(Brand brand) {

		return brandRepository.save(brand);
	}

	@Override
	public Void delete(Brand brand) {
		brandRepository.delete(brand);
		return null;
	}

}
