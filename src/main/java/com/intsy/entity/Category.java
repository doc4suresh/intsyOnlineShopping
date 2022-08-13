package com.intsy.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String catName;

	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	private List<Subcategory> subcategotyList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public List<Subcategory> getSubcategotyList() {
		return subcategotyList;
	}

	public void setSubcategotyList(List<Subcategory> subcategotyList) {
		this.subcategotyList = subcategotyList;
	}

	
}
