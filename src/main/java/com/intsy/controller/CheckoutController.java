package com.intsy.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.intsy.entity.BillingAddress;
import com.intsy.entity.CartItem;
import com.intsy.entity.Order;
import com.intsy.entity.Payment;
import com.intsy.entity.ShippingAddress;
import com.intsy.entity.ShoppingCart;
import com.intsy.entity.User;
import com.intsy.entity.UserPayment;
import com.intsy.entity.UserShipping;
import com.intsy.service.BillingAddressService;
import com.intsy.service.CartItemService;
import com.intsy.service.OrderService;
import com.intsy.service.PaymentService;
import com.intsy.service.ShippingAddressService;
import com.intsy.service.ShoppingCartService;
import com.intsy.service.UserService;
import com.intsy.utility.MailConstructor;
import com.intsy.utility.SLDistricts;

@Controller
public class CheckoutController {

	private ShippingAddress shippingAddress = new ShippingAddress();
	private Payment payment = new Payment();
	private BillingAddress billingAddress = new BillingAddress();

	@Autowired
	private UserService userService;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private ShippingAddressService shippingAddressService;

	@Autowired
	private BillingAddressService billingAddressService;
	
	@Autowired
	private OrderService orderService;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private MailConstructor mailConstructor;
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@RequestMapping("checkout")
	public String checkoutGET(@RequestParam("id") Long cartId,
			@RequestParam(value = "missingRequiredField", required = false) boolean missingRequiredField, Model model,
			Principal principal) {

		User user = userService.findByUsername(principal.getName());
		if (cartId != user.getShoppingCart().getId()) {
			return "badRequestPage";
		}

		List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());

		if (cartItemList.size() == 0) {
			model.addAttribute("emptyCart", true);
			return "redirect:/shoppingCart/cart";
		}

		for (CartItem cartItem : cartItemList) {

			if (cartItem.getItem().getQty() < cartItem.getQty()) {
				model.addAttribute("notEnoughStock", true);
				return "redirect:/shoppingCart/cart";
			}
		}

		List<UserShipping> userShippingList = user.getUserShippingList();
		List<UserPayment> userPaymentList = user.getUserPaymentList();

		model.addAttribute("userShippingList", userShippingList);
		model.addAttribute("userPaymentList", userPaymentList);

		if (userPaymentList.size() == 0) {
			model.addAttribute("emptyPayementList", true);

		} else {
			model.addAttribute("emptyPayementList", false);
		}

		for (UserShipping userShipping : userShippingList) {
			if (userShipping.isUserShippingDefault()) {
				shippingAddressService.setByUserShipping(userShipping, shippingAddress);

			}

		}

		for (UserPayment userPayment : userPaymentList) {

			if (userPayment.isDefaultPayment()) {

				paymentService.setByUserPayment(userPayment, payment);
				billingAddressService.setByUserBilling(userPayment.getUserBilling(), billingAddress);
			}
		}

		model.addAttribute("shippingAddress", shippingAddress);
		model.addAttribute("payment", payment);

		model.addAttribute("billingAddress", billingAddress);
		model.addAttribute("cartItemList", cartItemList);

		model.addAttribute("shoppingCart", user.getShoppingCart());

		List<String> districtList = SLDistricts.Districts;
		Collections.sort(districtList);
		model.addAttribute("districtList", districtList);
		model.addAttribute("classActiveShipping", true);

		int thisYear = LocalDate.now().getYear();
		List<Integer> expiryYearList = new ArrayList<>();
		for (int i = 0; i < 15; i++) {
			expiryYearList.add(thisYear + i);
		}
		model.addAttribute("expiryYearList", expiryYearList);

		if (missingRequiredField) {
			model.addAttribute("missingRequiredField", true);
		}

		return "checkout";
	}

	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String checkoutPOST(@ModelAttribute("shippingAddress") ShippingAddress shippingAddress,
			@ModelAttribute("billingAddress") BillingAddress billingAddress, @ModelAttribute("payment") Payment payment,
			@ModelAttribute("billingSameAsShipping") String billingSameAsShipping,
			@ModelAttribute("shippingMethod") String shippingMethod, Principal principal, Model model) {
		ShoppingCart shoppingCart = userService.findByUsername(principal.getName()).getShoppingCart();

		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		model.addAttribute("cartItemList", cartItemList);

		if (billingSameAsShipping.equals("true")) {
			billingAddress.setBillingAddressName(shippingAddress.getShippingAddressName());
			billingAddress.setBillingAddressStreet1(shippingAddress.getShippingAddressStreet1());
			billingAddress.setBillingAddressStreet2(shippingAddress.getShippingAddressStreet2());
			billingAddress.setBillingAddressCity(shippingAddress.getShippingAddressCity());
			billingAddress.setBillingAddressDistrict(shippingAddress.getShippingAddressDistrict());
			billingAddress.setBillingAddressPostalCode(shippingAddress.getShippingAddressPostalCode());
		}

		if (shippingAddress.getShippingAddressStreet1().isEmpty() || shippingAddress.getShippingAddressCity().isEmpty()
				|| shippingAddress.getShippingAddressDistrict().isEmpty()
				|| shippingAddress.getShippingAddressName().isEmpty()
				|| shippingAddress.getShippingAddressPostalCode().isEmpty() || payment.getCardNumber().isEmpty()
				|| payment.getCvv() == 0 || billingAddress.getBillingAddressStreet1().isEmpty()
				|| billingAddress.getBillingAddressCity().isEmpty()
				|| billingAddress.getBillingAddressDistrict().isEmpty()
				|| billingAddress.getBillingAddressName().isEmpty()
				|| billingAddress.getBillingAddressPostalCode().isEmpty())
			return "redirect:/checkout?id=" + shoppingCart.getId() + "&missingRequiredField=true";

		User user = userService.findByUsername(principal.getName());

		Order order = orderService.createOrder(shoppingCart, shippingAddress, billingAddress, payment, shippingMethod,
				user);

		mailSender.send(mailConstructor.constructOrderConfirmationEmail(user, order, Locale.ENGLISH));

		shoppingCartService.clearShoppingCart(shoppingCart);

		LocalDate today = LocalDate.now();
		LocalDate estimatedDeliveryDate;

		if (shippingMethod.equals("groundShipping")) {
			estimatedDeliveryDate = today.plusDays(5);
		} else {
			estimatedDeliveryDate = today.plusDays(3);
		}

		model.addAttribute("estimatedDeliveryDate", estimatedDeliveryDate);

		return "orderSubmittedPage";
	}

}
