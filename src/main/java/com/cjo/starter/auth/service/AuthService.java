package com.cjo.starter.auth.service;

import java.util.List;

import com.cjo.starter.auth.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cjo.starter.auth.domain.JWTPayload;
import com.cjo.starter.auth.domain.Role;
import com.cjo.starter.auth.model.User;
import com.cjo.starter.auth.model.UserRole;
import com.cjo.starter.auth.repository.UserCRUDRepository;
import com.cjo.starter.auth.repository.UserRoleCRUDRepository;
import com.cjo.starter.common.BaseService;
import com.cjo.starter.common.exception.UserException;

/**
 * 
 * The class AuthService<br>
 * <br>
 * A service to handle all authentication logic .<br>
 * <br>
 * @author Tomas
 * @version 1.0
 * @since Feb 20, 2019
 *
 */
@Service
public class AuthService extends BaseService {

	@Autowired
	private UserCRUDRepository userRepository;

	@Autowired
	private UserRoleCRUDRepository userRoleRepository;
	
	@Autowired
	private JWTAuthenticateService jwtAuthenticateService;

	@Autowired
	private PasswordEncodingService passwordEncodingService;

	@Autowired
	private AuthRepository authRepository;

	private String prepareJwtToken(final String email, final Role role) {
		JWTPayload payload = new JWTPayload().setEmail(email).setRole(role.role);
		return jwtAuthenticateService.createToken(email, payload).get();
	}

	public String signup(final String email, final String password) {
		User user = new User(email, passwordEncodingService.hashpw(password));
		UserRole userRole = new UserRole(Role.ROLE_USER.role);
		userRole.setUser(user);
		
		String token = prepareJwtToken(email, Role.ROLE_USER);
		if (token != null) {
			user.setToken(token);
			user = userRepository.save(user);
			userRoleRepository.save(userRole);
			return token;
		} else {
			return null;
		}
	}

	public String signin(final String email, final String password) throws UserException {
		List<User> users = userRepository.findByEmail(email);
		if (users.isEmpty()) {
			throw new UserException("MSG_AUTH_0003");
		}
		
		User user = users.get(0);
		if (!passwordEncodingService.check(password, user.getPassword())) {
			throw new UserException("MSG_AUTH_0003");
		}
		
		String token = prepareJwtToken(email, Role.fromString(user.getUserRole().getRoleName()));
		if (token != null) {
			user.setToken(token);
			user = userRepository.save(user);
			return token;
		}
		return null;
	}

}
