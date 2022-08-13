package com.intsy.controller;

import java.security.Principal;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.intsy.entity.Category;
import com.intsy.entity.Item;
import com.intsy.entity.Subcategory;
import com.intsy.entity.User;
import com.intsy.service.CategoryService;
import com.intsy.service.ItemService;
import com.intsy.service.SubcategoryService;
import com.intsy.service.UserService;

@Controller
public class ItemController {

	@Autowired
	private Environment env;

	@Autowired
	UserService userService;

	@Autowired
	ItemService itemService;

	@Autowired
	CategoryService categoryService;

	@Autowired
	SubcategoryService subcategoryService;

//	private double distributorDiscount = Double.parseDouble(env.getProperty("distributor.discount"));
//	private double retailDiscount = Double.parseDouble(env.getProperty("retail.discount"));

	@GetMapping("/items")
	public String itemList(Model model, Principal principal) {

		if (principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user",user);
			model.addAttribute("showPrice", true);

			
		} else {
			model.addAttribute("showPrice", false);

		}

		List<Item> itemList = itemService.findAllActive();
		List<Category> catList = categoryService.findAll();
		List<Subcategory> subcatList = subcategoryService.findAll();
		model.addAttribute("itemList", itemList);

		 
		
		model.addAttribute("catList", catList);
		model.addAttribute("subcatList", subcatList);
		model.addAttribute("activeAll", true);
		

		return "items";

	}

	@RequestMapping("/itemInfo")
	public String itmeInfoGet(@PathParam("id") Long id, Model model, Principal principal) {

		if (principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);

			model.addAttribute("showPrice", true);
		} else {
			model.addAttribute("showPrice", false);

		}

		Item item = itemService.findOne(id);
		model.addAttribute("item", item);
//		model.addAttribute("discount",discount);

		return "itemInfo";
	}

	@GetMapping("/searchByCategory")
	public String searchByCategory(@RequestParam("category") Category category, Model model, Principal principal) {

		if (principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);

		}
		List<Category> catList = categoryService.findAll();
		List<Subcategory> subcatList = subcategoryService.findAll();

		model.addAttribute("catList", catList);
		model.addAttribute("subcatList", subcatList);

		List<Item> itemList = itemService.findByCategory(category);
		if (itemList.isEmpty()) {

			model.addAttribute("emptyList", true);

		}
		model.addAttribute("itemList", itemList);
		return "items";
	}

	@GetMapping("/searchBySubcategory")
	public String searchBySubcategory(@RequestParam("subcategory") Subcategory subcategory, Model model,
			Principal principal) {

		if (principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}

		List<Category> catList = categoryService.findAll();
		List<Subcategory> subcatList = subcategoryService.findSubcategoriesByCatId(subcategory.getCategory().getId());

		model.addAttribute("catList", catList);
		model.addAttribute("subcatList", subcatList);

		List<Item> itemList = itemService.findBySubcategory(subcategory);
		if (itemList.isEmpty()) {

			model.addAttribute("emptyList", true);
		}
		model.addAttribute("itemList", itemList);
		return "items";
	}

	@GetMapping("/specials")
	public String specialGet(Model model) {

		List<Item> itemList = itemService.findBySpecials();

		if (itemList.isEmpty()) {

			model.addAttribute("emptyList", true);
		}

		model.addAttribute("itemList", itemList);

		return "specials";

	}

	@GetMapping("/newArrivals")
	public String newArrivalsGet(Model model) {

		List<Item> itemList = itemService.findByNewArrivals();

		if (itemList.isEmpty()) {

			model.addAttribute("emptyList", true);
		}

		model.addAttribute("itemList", itemList);

		return "newArrivals";

	}
}
