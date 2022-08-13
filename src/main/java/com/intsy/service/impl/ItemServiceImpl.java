package com.intsy.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intsy.entity.Category;
import com.intsy.entity.Item;
import com.intsy.entity.Subcategory;
import com.intsy.repository.ItemRepository;
import com.intsy.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService  {

	@Autowired
	private ItemRepository itemRepository;

	@Override
	public Item save(Item item) {

		return itemRepository.save(item);
	}

	@Override
	public Item findOne(Long id) {

		return itemRepository.getOne(id);
	}

	@Override
	public List<Item> findAll() {
		return itemRepository.findAll();
	}

	@Override
	public void update(Item item) {

	}

	@Override
	public List<Item> findAllActive() {
		List<Item> items = itemRepository.findAll();
		List<Item> activeItems = new ArrayList<>();

		for (Item item : items) {
			if (item.isActive()) {
				activeItems.add(item);

			}

		}

		return activeItems;
	}

	@Override
	public List<Item> findByCategory(Category category) {

		List<Subcategory> subcategoryList = category.getSubcategotyList();

		List<Item> itemList = new ArrayList<>();
		for (Subcategory subcategory : subcategoryList) {
			itemList.addAll(subcategory.getItemList());
		}

		List<Item> activeItemList = new ArrayList<>();
		for (Item item : itemList) {
			if (item.isActive()) {
				activeItemList.add(item);

			}
		}
		return activeItemList;
	}

	@Override
	public List<Item> findBySubcategory(Subcategory subcategory) {

		List<Item> itemList = new ArrayList<>();

		itemList.addAll(subcategory.getItemList());

		List<Item> activeItemList = new ArrayList<>();
		for (Item item : itemList) {
			if (item.isActive()) {
				activeItemList.add(item);

			}
		}
		return activeItemList;
	}

	@Override
	public List<Item> findBySpecials() {
		
		List<Item> items = itemRepository.findAll();
		List<Item> specialItems = new ArrayList<>();

		for (Item item : items) {
			if (item.isActive() & item.isSpecial()) {
				specialItems.add(item);

			}

		}
		return specialItems ;
	}

	@Override
	public List<Item> findByNewArrivals() {
		List<Item> items = itemRepository.findAll();
		List<Item> activeList = new ArrayList<Item>();
		
		for (Item item : items) {
			if (item.isActive()) {
				activeList.add(item);
			}
		}
		
		Collections.sort(activeList, new DateCampare());
		List<Item> newArrivals = new ArrayList<>();
		
		for(int i = 0 ; i < 10 ; i++) {
			newArrivals.add(activeList.get(i));
		}
		return newArrivals ;
	}

	
}

class DateCampare implements Comparator<Item>{
	@Override
	 public int compare(Item item1, Item item2) {
		
		if (item2.getAddDate() == null) {
	        return (item1.getAddDate() == null) ? 0 : -1;
	    }
	    if (item1.getAddDate() == null) {
	        return 1;
	    }
	    
		return item2.getAddDate().compareTo(item1.getAddDate());

	
	  }
}