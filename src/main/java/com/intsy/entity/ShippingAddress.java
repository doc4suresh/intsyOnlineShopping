package com.intsy.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class ShippingAddress {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String shippingAddressName;
	private String shippingAddressStreet1;
	private String shippingAddressStreet2;
	private String shippingAddressCity;
	private String shippingAddressDistrict;
	private String shippingAddressPostalCode;
	
	
	@OneToOne
	private Order order;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getShippingAddressName() {
		return shippingAddressName;
	}


	public void setShippingAddressName(String shippingAddressName) {
		this.shippingAddressName = shippingAddressName;
	}


	public String getShippingAddressStreet1() {
		return shippingAddressStreet1;
	}


	public void setShippingAddressStreet1(String shippingAddressStreet1) {
		this.shippingAddressStreet1 = shippingAddressStreet1;
	}


	public String getShippingAddressStreet2() {
		return shippingAddressStreet2;
	}


	public void setShippingAddressStreet2(String shippingAddressStreet2) {
		this.shippingAddressStreet2 = shippingAddressStreet2;
	}


	public String getShippingAddressCity() {
		return shippingAddressCity;
	}


	public void setShippingAddressCity(String shippingAddressCity) {
		this.shippingAddressCity = shippingAddressCity;
	}


	public String getShippingAddressDistrict() {
		return shippingAddressDistrict;
	}


	public void setShippingAddressDistrict(String shippingAddressDistrict) {
		this.shippingAddressDistrict = shippingAddressDistrict;
	}

	public String getShippingAddressPostalCode() {
		return shippingAddressPostalCode;
	}


	public void setShippingAddressPostalCode(String shippingAddressPostalCode) {
		this.shippingAddressPostalCode = shippingAddressPostalCode;
	}


	public Order getOrder() {
		return order;
	}


	public void setOrder(Order order) {
		this.order = order;
	}

}
