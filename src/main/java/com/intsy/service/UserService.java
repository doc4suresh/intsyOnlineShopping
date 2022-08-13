package com.intsy.service;

import java.util.Set;

import com.intsy.entity.User;
import com.intsy.entity.UserBilling;
import com.intsy.entity.UserPayment;
import com.intsy.entity.UserShipping;
import com.intsy.entity.security.PasswordResetToken;
import com.intsy.entity.security.UserRole;

public interface UserService {

	PasswordResetToken getPasswordResetToken(final String token);

	void createPasswordResetTokenForUser(final User user, final String token);

	User findByUsername(String username);

	User createUser(User user, Set<UserRole> userRoles) throws Exception;

	User updateUser(User user) throws Exception;
	
	User save(User user);

	void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user);

	void updateUserShipping(UserShipping userShipping, User user);

	void setUserDefaultPayment(Long userPaymentId, User user);

	void setUserDefaultShipping(Long userShippingId, User user);
	
	
}
