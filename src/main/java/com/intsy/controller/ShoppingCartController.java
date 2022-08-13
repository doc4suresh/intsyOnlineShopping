package com.intsy.controller;

import java.security.Principal;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.intsy.entity.CartItem;
import com.intsy.entity.Item;
import com.intsy.entity.ShoppingCart;
import com.intsy.entity.User;
import com.intsy.service.CartItemService;
import com.intsy.service.ItemService;
import com.intsy.service.ShoppingCartService;
import com.intsy.service.UserService;

@Controller
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

	@Autowired
	private Environment env;

	@Autowired
	private UserService userService;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private ShoppingCartService shoppingCartService;

	@Autowired
	private ItemService itemService;

	private boolean notEnoughStock;

//	private double distributorDiscount = Double.parseDouble(env.getProperty("distributor.discount"));
//	private double retailDiscount = Double.parseDouble(env.getProperty("retail.discount"));

	@GetMapping("/cart")
	public String shoppingCart(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());

		ShoppingCart shoppingCart = user.getShoppingCart();

		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		if (shoppingCart != null) {
			shoppingCartService.updateShoppingCart(shoppingCart, user);

			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("shoppingCart", shoppingCart);
		} else {
			model.addAttribute("nothingAdded", true);
		}
		model.addAttribute("notEnoughStock", notEnoughStock);

		return "shoppingCart";

	}

	@RequestMapping("/addItemToCart")
	public String addToCart(@ModelAttribute("item") Item item, @ModelAttribute("qty") String qty, Model model,
			Principal principal) {

		User user = userService.findByUsername(principal.getName());
		Long id = item.getItemId();
		Item currentItem = itemService.findOne(id);

		if (Integer.parseInt(qty) > currentItem.getQty()) {
			model.addAttribute("notEnoughStock", true);
			return "forward:/itemInfo?id=" + currentItem.getItemId();

		}
		CartItem cartItem = cartItemService.addItemToCartItem(currentItem, user, Integer.parseInt(qty));

		model.addAttribute("addItemSuccess", true);

		return "forward:/itemInfo?id=" + currentItem.getItemId();

	}

	@RequestMapping("/removeItemFromCart")
	public String removeFromCartItem(@PathParam("id") Long id) {

		CartItem cartItem = cartItemService.findById(id);
		cartItemService.removeFromCartItem(cartItem);

		return "redirect:/shoppingCart/cart";
	}

	@RequestMapping("/updateCartItem")
	public String updateCartItem(@ModelAttribute("id") Long cartItemId, @ModelAttribute("qty") int qty, Model model,
			Principal principal) {

		CartItem cartItem = cartItemService.findById(cartItemId);

		if (qty > cartItem.getQty()) {
			// model.addAttribute("notEnoughStock",true);
			notEnoughStock = true;
			return "redirect:/shoppingCart/cart";

		}
		User user = userService.findByUsername(principal.getName());
		notEnoughStock = false;
		cartItem.setQty(qty);
		cartItemService.updateCartItem(cartItem, user);

		return "redirect:/shoppingCart/cart";
	}

}
