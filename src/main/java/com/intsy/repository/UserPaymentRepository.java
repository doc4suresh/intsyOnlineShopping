package com.intsy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intsy.entity.UserPayment;

public interface UserPaymentRepository extends JpaRepository<UserPayment, Long>{

}
