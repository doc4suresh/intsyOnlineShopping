package com.intsy.utility;

import java.util.Locale;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.intsy.entity.Order;
import com.intsy.entity.User;

@Component
public class MailConstructor {

	@Autowired
	private Environment env;
	
	@Autowired
	private TemplateEngine templateEngine;

	public SimpleMailMessage constructorResetTokenEmail(String contextPath, Locale locale, String token, User user,
			String password, String method) {

		// String url = contextPath + "/newCusEmail?token=" + token;

		String url = contextPath + "/" + method + "?token=" + token;

		String message = "\nPlease click on this link to verify your email and edit your personal information. Your Token is : \n"
				+ token;

		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(user.getUsername());
		email.setSubject("Intsy Pvt Ltd : User Verification");
		email.setText(url + message);
		email.setFrom(env.getProperty("support.email"));

		return email;

	}

	public MimeMessagePreparator constructOrderConfirmationEmail(User user, Order order, Locale locale) {
		Context context = new Context();
		context.setVariable("order", order);
		context.setVariable("user", user);
		context.setVariable("cartItemList", order.getCartItemList());
		String text = templateEngine.process("orderConfirmationEmailTemplate", context);

		MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
				email.setTo(user.getUsername());
				email.setSubject("Intsy Pvt Ltd : Order Confirmation - " + order.getId());
				email.setText(text, true);
				email.setFrom(env.getProperty("support.email"));
			}
		};

		return messagePreparator;
	}
}
