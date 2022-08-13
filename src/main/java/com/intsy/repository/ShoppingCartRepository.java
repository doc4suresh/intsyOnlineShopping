package com.intsy.repository;

import org.springframework.data.repository.CrudRepository;

import com.intsy.entity.ShoppingCart;

public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, Long> {

}
