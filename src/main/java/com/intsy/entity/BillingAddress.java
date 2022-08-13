package com.intsy.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class BillingAddress {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String billingAddressName;
	private String billingAddressStreet1;
	private String billingAddressStreet2;
	private String billingAddressCity;
	private String billingAddressDistrict;
	private String billingAddressPostalCode;
	
	@OneToOne
	private Order order;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBillingAddressName() {
		return billingAddressName;
	}

	public void setBillingAddressName(String billingAddressName) {
		this.billingAddressName = billingAddressName;
	}

	public String getBillingAddressStreet1() {
		return billingAddressStreet1;
	}

	public void setBillingAddressStreet1(String billingAddressStreet1) {
		this.billingAddressStreet1 = billingAddressStreet1;
	}

	public String getBillingAddressStreet2() {
		return billingAddressStreet2;
	}

	public void setBillingAddressStreet2(String billingAddressStreet2) {
		this.billingAddressStreet2 = billingAddressStreet2;
	}

	public String getBillingAddressCity() {
		return billingAddressCity;
	}

	public void setBillingAddressCity(String billingAddressCity) {
		this.billingAddressCity = billingAddressCity;
	}

	public String getBillingAddressDistrict() {
		return billingAddressDistrict;
	}

	public void setBillingAddressDistrict(String billingAddressDistrict) {
		this.billingAddressDistrict = billingAddressDistrict;
	}



	public String getBillingAddressPostalCode() {
		return billingAddressPostalCode;
	}

	public void setBillingAddressPostalCode(String billingAddressPostalCode) {
		this.billingAddressPostalCode = billingAddressPostalCode;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}



}
