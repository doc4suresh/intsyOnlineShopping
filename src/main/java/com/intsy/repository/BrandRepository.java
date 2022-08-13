package com.intsy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intsy.entity.Brand;



public interface BrandRepository extends JpaRepository<Brand, Long> {

}
