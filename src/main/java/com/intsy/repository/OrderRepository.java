package com.intsy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intsy.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}
