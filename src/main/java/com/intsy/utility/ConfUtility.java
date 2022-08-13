package com.intsy.utility;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ConfUtility {
	@Autowired
	private Environment environment;

	private static Environment env;

	@PostConstruct
	public void init() {
		env = environment;
	}

	public final static double DISTRIBUTOR_DISCOUNT() {
		return Double.parseDouble(env.getProperty("distributor.discount"));
	}

	public final static double RETAIL_DISCOUNT() {
		return Double.parseDouble(env.getProperty("retail.discount"));
	}
}
