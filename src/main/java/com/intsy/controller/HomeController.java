package com.intsy.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.intsy.entity.CartItem;
import com.intsy.entity.Order;
import com.intsy.entity.User;
import com.intsy.entity.UserBilling;
import com.intsy.entity.UserPayment;
import com.intsy.entity.UserShipping;
import com.intsy.entity.security.PasswordResetToken;
import com.intsy.entity.security.Role;
import com.intsy.entity.security.UserRole;
import com.intsy.service.CartItemService;
import com.intsy.service.OrderService;
import com.intsy.service.RoleService;
import com.intsy.service.UserPaymentService;
import com.intsy.service.UserService;
import com.intsy.service.UserShippingService;
import com.intsy.utility.MailConstructor;
import com.intsy.utility.SLDistricts;
import com.intsy.utility.SecurityUtility;

@Controller
public class HomeController {

	@Autowired
	private UserService userService;

	@Autowired
	private MailConstructor mailConstructor;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private UserPaymentService userPaymentService;

	@Autowired
	private UserShippingService userShippingService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private RoleService roleService;

	/**
	 * Get mapping for customerReg
	 * 
	 * @return customerReg view
	 */
	@GetMapping("/customerReg")
	public String customerRegGet(Model model) {
		// model.addAttribute("classActiveLogin", true);

		List<String> districtList = SLDistricts.Districts;
		Collections.sort(districtList);
		model.addAttribute("districtList", districtList);
		return "customerReg";
	}

// ----------Step 1 of the customer registration process - fill the registration form and submit------
	/**
	 * New user registration and request for approval
	 * 
	 * @param request
	 * @param useremail
	 * @param regType
	 * @param firstName
	 * @param lastName
	 * @param mobile
	 * @param companyName
	 * @param companyRegNo
	 * @param address1
	 * @param address2
	 * @param city
	 * @param district
	 * @param comment
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/customerReg")
	public String customerRegPost(HttpServletRequest request, @ModelAttribute("email") String username,
			@ModelAttribute("regType") String regType, @ModelAttribute("firstName") String firstName,
			@ModelAttribute("lastName") String lastName,

			@ModelAttribute("mobile") String mobile, @ModelAttribute("companyName") String companyName,
			@ModelAttribute("companyRegNo") String companyRegNo, @ModelAttribute("address1") String address1,

			@ModelAttribute("address2") String address2, @ModelAttribute("city") String city,
			@ModelAttribute("district") String district, @ModelAttribute("comment") String comment,

			Model model) throws Exception {

//		model.addAttribute("classActiveNewAccount", true);

		if (userService.findByUsername(username) != null) {
			model.addAttribute("emailExists", true);

			List<String> districtList = SLDistricts.Districts;
			Collections.sort(districtList);
			model.addAttribute("districtList", districtList);
			
			return "customerReg";
		}

		User user = new User();
		user.setUsername(username);
		String password = SecurityUtility.randomPassword();
		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encryptedPassword);

		user.setRegType(regType);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setMobile(mobile);

		user.setCompanyName(companyName);
		user.setCompanyRegNo(companyRegNo);
		user.setAddress1(address1);
		user.setAddress2(address2);

		user.setCity(city);
		user.setDistrict(district);
		user.setComment(comment);

		user.setStatus("Initial");

	
		Role role = roleService.findByName("Customer");
		

		Set<UserRole> userRoles = new HashSet<>();
		userRoles.add(new UserRole(user, role));
	
		userService.createUser(user, userRoles);
		
		List<String> districtList = SLDistricts.Districts;
		Collections.sort(districtList);
		model.addAttribute("districtList", districtList);

		model.addAttribute("registrationSent", true);

		return "customerReg";
	}

	// ----------Step2 of the customer registration process -- verify through email
	// link -------
	/**
	 * Email verification with Temporary Token
	 * 
	 * @param locale
	 * @param token  - token parameter from the url
	 * @param model
	 * @return
	 */
	@GetMapping("/newCusEmail")
	public String newCusEmailGet(Locale locale, @RequestParam("token") String token, Model model) {

		PasswordResetToken passwordResetToken = userService.getPasswordResetToken(token);
		if (passwordResetToken == null) {
			String message = "Invalid Token";
			model.addAttribute("message", message);
			return "redirect:/badRequest";
		}

		User user = passwordResetToken.getUser();
		String userEmail = user.getUsername();
		UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		model.addAttribute("user", user);
		model.addAttribute("pRToken", passwordResetToken);
		model.addAttribute("classActiveEdit", true);

		return "newCusEmail";
	}

	// ----------Step 3 --- user provide a new password and submit ---------------
	/**
	 * Update user information and New password with token verification.
	 * 
	 * @param request
	 * @param useremail
	 * @param token
	 * @param password
	 * @param regType
	 * @param firstName
	 * @param lastName
	 * @param mobile
	 * @param companyName
	 * @param companyRegNo
	 * @param address1
	 * @param address2
	 * @param city
	 * @param district
	 * @param comment
	 * @param model
	 * @return
	 * @throws Exception
	 */

	@PostMapping("/newCusEmail")
	public String newCusEmailPost(HttpServletRequest request, @ModelAttribute("email") String username,
			@ModelAttribute("token") String token, @ModelAttribute("newPassword") String password,

			Model model) throws Exception {

		model.addAttribute("classActiveEdit", true);
		model.addAttribute("email", username);

		PasswordResetToken passwordResetToken = userService.getPasswordResetToken(token);
		if (passwordResetToken == null) {
			String message = "Invalid Token";
			model.addAttribute("message", message);
			return "redirect:/badRequest";
		}

		User user = userService.findByUsername(username);
		if (user == null) {
			return "redirect:/badRequest";
		}

		user.setUsername(username);
		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encryptedPassword);

		userService.updateUser(user);

		model.addAttribute("userUpdated", true);

		return "index";
	}

	// ------------------------------Login request -------------------------
	/**
	 * Login Get request *** Login Post request handle by Spring security
	 * 
	 * * @return login form
	 */
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	// -----------------------------Reset
	// Password-----------------------------------
	@GetMapping("/resetPassword")
	public String resetPasswordGet() {
		return "resetPassword";
	}

	@RequestMapping(value = "resetPassword", method = RequestMethod.POST)
	public String resetPasswordPost(HttpServletRequest request, @ModelAttribute("username") String username,
			Model model) throws Exception {

		User user = userService.findByUsername(username);

		if (user == null) {
			model.addAttribute("emailNotExist", true);
			return "resetPassword";
		}

		String password = SecurityUtility.randomPassword();
		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encryptedPassword);
		userService.updateUser(user);

		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(user, token);

		String appurl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

		String method = "newCusEmail";

		SimpleMailMessage newMessage = mailConstructor.constructorResetTokenEmail(appurl, request.getLocale(), token,
				user, encryptedPassword, method);

		mailSender.send(newMessage);

		model.addAttribute("forgotPasswordEmailSent", true);

		return "resetPassword";
	}

	// ------------------------------------------My Profile
	// -----------------------------------------------------
	@RequestMapping("/myProfile")
	public String myProfile(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("orderList", user.getOrderList());

		UserShipping userShipping = new UserShipping();
		model.addAttribute("userShipping", userShipping);

		List<String> districtList = SLDistricts.Districts;
		Collections.sort(districtList);
		model.addAttribute("districtList", districtList);

		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("listOfShippingAddresses", true);
		model.addAttribute("classActiveEdit", true);

		return "myProfile";
	}

	// ------------------------------List of Credit Cards Saved Customer in Customer
	// Profile --------------------------------------
	@RequestMapping("/listOfCreditCards")
	public String listOfCreditCards(Model model, Principal principal, HttpServletRequest request) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("orderList", user.getOrderList());

		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("listOfShippingAddresses", true);
		model.addAttribute("classActiveBilling", true);

		return "myProfile";
	}

	// -----------------------------------List of Shipping Addresses Saved in
	// Customer profile--------------------
	@RequestMapping("/listOfShippingAddresses")
	public String listOfShippingAddresses(Model model, Principal principal, HttpServletRequest request) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("orderList", user.getOrderList());

		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("listOfShippingAddresses", true);
		model.addAttribute("classActiveShipping", true);

		return "myProfile";
	}

	// ---------------------------------------Customer Adding a New Credit Card
	// ------------------------------
	@RequestMapping("/addNewCreditCard")
	public String addNewCreditCard(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		UserBilling userBilling = new UserBilling();
		UserPayment userPayment = new UserPayment();

		model.addAttribute("userBilling", userBilling);
		model.addAttribute("userPayment", userPayment);

		List<String> districtList = SLDistricts.Districts;
		Collections.sort(districtList);
		model.addAttribute("districtList", districtList);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("orderList", user.getOrderList());

		model.addAttribute("addNewCreditCard", true);
		model.addAttribute("listOfShippingAddresses", true);
		model.addAttribute("classActiveBilling", true);

		return "myProfile";
	}

	@RequestMapping(value = "/addNewCreditCard", method = RequestMethod.POST)
	public String addNewCreditCard(@ModelAttribute("userPayment") UserPayment userPayment,
			@ModelAttribute("userBilling") UserBilling userBilling, Principal principal, Model model) {
		User user = userService.findByUsername(principal.getName());
		userService.updateUserBilling(userBilling, userPayment, user);

		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("orderList", user.getOrderList());

		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("listOfShippingAddresses", true);
		model.addAttribute("classActiveBilling", true);

		return "myProfile";
	}

	@RequestMapping("/updateCreditCard")
	public String updateCreditCard(@ModelAttribute("id") Long creditCardId, Principal principal, Model model) {
		User user = userService.findByUsername(principal.getName());
		UserPayment userPayment = userPaymentService.findById(creditCardId);

		if (user.getId() != userPayment.getUser().getId()) {
			return "badRequestPage";
		} else {
			model.addAttribute("user", user);
			UserBilling userBilling = userPayment.getUserBilling();
			model.addAttribute("userPayment", userPayment);
			model.addAttribute("userBilling", userBilling);

			List<String> districtList = SLDistricts.Districts;
			Collections.sort(districtList);
			model.addAttribute("districtList", districtList);

			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			model.addAttribute("orderList", user.getOrderList());

			model.addAttribute("addNewCreditCard", true);
			model.addAttribute("listOfShippingAddresses", true);
			model.addAttribute("classActiveBilling", true);

			return "myProfile";
		}
	}

	@RequestMapping("/removeCreditCard")
	public String removeCreditCard(@ModelAttribute("id") Long creditCardId, Principal principal, Model model) {
		User user = userService.findByUsername(principal.getName());
		UserPayment userPayment = userPaymentService.findById(creditCardId);

		if (user.getId() != userPayment.getUser().getId()) {
			return "badRequestPage";
		} else {
			model.addAttribute("user", user);
			userPaymentService.removeById(creditCardId);

			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			model.addAttribute("orderList", user.getOrderList());

			model.addAttribute("listOfCreditCards", true);
			model.addAttribute("listOfShippingAddresses", true);
			model.addAttribute("classActiveBilling", true);

			return "myProfile";
		}
	}

	@RequestMapping(value = "/setDefaultPayment", method = RequestMethod.POST)
	public String setDefaultPayment(@ModelAttribute("defaultUserPaymentId") Long defaultPaymentId, Principal principal,
			Model model) {
		User user = userService.findByUsername(principal.getName());
		userService.setUserDefaultPayment(defaultPaymentId, user);

		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("orderList", user.getOrderList());

		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("listOfShippingAddresses", true);
		model.addAttribute("classActiveBilling", true);

		return "myProfile";
	}

	// -----------------------------------Customer Adding a New
	// Address----------------------------------------
	@RequestMapping("/addNewShippingAddress")
	public String addNewShippingAddress(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);

		UserShipping userShipping = new UserShipping();
		model.addAttribute("userShipping", userShipping);

		List<String> districtList = SLDistricts.Districts;
		Collections.sort(districtList);
		model.addAttribute("districtList", districtList);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("orderList", user.getOrderList());

		model.addAttribute("addNewShippingAddress", true);
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveShipping", true);

		return "myProfile";
	}

	@RequestMapping(value = "/addNewShippingAddress", method = RequestMethod.POST)
	public String addNewShippingAddressPost(@ModelAttribute("userShipping") UserShipping userShipping,
			Principal principal, Model model) {
		User user = userService.findByUsername(principal.getName());
		userService.updateUserShipping(userShipping, user);

		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("orderList", user.getOrderList());

		model.addAttribute("listOfShippingAddresses", true);
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveShipping", true);

		return "myProfile";
	}

	@RequestMapping("/updateUserShipping")
	public String updateUserShipping(@ModelAttribute("id") Long shippingAddressId, Principal principal, Model model) {
		User user = userService.findByUsername(principal.getName());
		UserShipping userShipping = userShippingService.findById(shippingAddressId);

		if (user.getId() != userShipping.getUser().getId()) {
			return "badRequestPage";
		} else {
			model.addAttribute("user", user);

			model.addAttribute("userShipping", userShipping);

			List<String> districtList = SLDistricts.Districts;
			Collections.sort(districtList);
			model.addAttribute("districtList", districtList);

			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			model.addAttribute("orderList", user.getOrderList());

			model.addAttribute("addNewShippingAddress", true);
			model.addAttribute("listOfCreditCards", true);
			model.addAttribute("classActiveShipping", true);

			return "myProfile";
		}
	}

	@RequestMapping(value = "/setDefaultShippingAddress", method = RequestMethod.POST)
	public String setDefaultShippingAddress(@ModelAttribute("defaultShippingAddressId") Long defaultShippingId,
			Principal principal, Model model) {
		User user = userService.findByUsername(principal.getName());
		userService.setUserDefaultShipping(defaultShippingId, user);

		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("orderList", user.getOrderList());

		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("listOfShippingAddresses", true);
		model.addAttribute("classActiveShipping", true);

		return "myProfile";
	}

	@RequestMapping("/removeUserShipping")
	public String removeUserShipping(@ModelAttribute("id") Long userShippingId, Principal principal, Model model) {
		User user = userService.findByUsername(principal.getName());
		UserShipping userShipping = userShippingService.findById(userShippingId);

		if (user.getId() != userShipping.getUser().getId()) {
			return "badRequestPage";
		} else {
			model.addAttribute("user", user);

			userShippingService.removeById(userShippingId);

			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			model.addAttribute("orderList", user.getOrderList());

			model.addAttribute("listOfShippingAddresses", true);
			model.addAttribute("classActiveShipping", true);

			return "myProfile";
		}
	}

	@RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
	public String updateUserInfo(@ModelAttribute("user") User user, @ModelAttribute("newPassword") String newPassword,
			Model model) throws Exception {
		User currentUser = userService.findByUsername(user.getUsername());

		if (currentUser == null) {
			throw new Exception("User not found");
		}

		/* check username already exists */
		if (userService.findByUsername(user.getUsername()) != null) {
			if (userService.findByUsername(user.getUsername()).getId() != currentUser.getId()) {
				model.addAttribute("usernameExists", true);
				return "myProfile";
			}
		}

		//update password
		if (newPassword != null && !newPassword.isEmpty() && !newPassword.equals("")) {
			BCryptPasswordEncoder passwordEncoder = SecurityUtility.passwordEncoder();
			String dbPassword = currentUser.getPassword();
			if (passwordEncoder.matches(user.getPassword(), dbPassword)) {
				currentUser.setPassword(passwordEncoder.encode(newPassword));
			} else {
				model.addAttribute("incorrectPassword", true);

				return "myProfile";
			}
		}

		currentUser.setFirstName(user.getFirstName());
		currentUser.setLastName(user.getLastName());
		currentUser.setUsername(user.getUsername());

		userService.save(currentUser);

		
		model.addAttribute("user", currentUser);
		model.addAttribute("classActiveEdit", true);

		model.addAttribute("updateSuccess", true);
		model.addAttribute("listOfShippingAddresses", true);
		model.addAttribute("listOfCreditCards", true);

		UserDetails userDetails = userDetailsService.loadUserByUsername(currentUser.getUsername());

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authentication);
		model.addAttribute("orderList", user.getOrderList());

		return "myProfile";
	}

	@RequestMapping("/orderDetail")
	public String orderDetail(@RequestParam("id") Long orderId, Principal principal, Model model) {
		User user = userService.findByUsername(principal.getName());
		Order order = orderService.findOne(orderId);

		if (order.getUser().getId() != user.getId()) {
			return "badRequestPage";
		} else {
			List<CartItem> cartItemList = cartItemService.findByOrder(order);
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("user", user);
			model.addAttribute("order", order);

			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			model.addAttribute("orderList", user.getOrderList());

			UserShipping userShipping = new UserShipping();
			model.addAttribute("userShipping", userShipping);

			List<String> districtList = SLDistricts.Districts;
			Collections.sort(districtList);
			model.addAttribute("districtList", districtList);

			model.addAttribute("listOfShippingAddresses", true);
			model.addAttribute("listOfCreditCards", true);
			model.addAttribute("displayOrderDetail", true);

			model.addAttribute("classActiveOrders", true);

			return "myProfile";
		}
	}
}
