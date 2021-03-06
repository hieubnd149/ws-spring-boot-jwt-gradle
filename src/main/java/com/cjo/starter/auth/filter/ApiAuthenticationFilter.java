package com.cjo.starter.auth.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import com.cjo.starter.auth.domain.Token;
import com.cjo.starter.auth.service.JWTAuthenticateService;

/**
 * 
 * The class ApiAuthenticationFilter<br>
 * <br>
 * Filter for checking access token.<br>
 * <br>
 * @author Tomas
 * @version 1.0
 * @since Feb 20, 2019
 *
 */
@Component
public class ApiAuthenticationFilter extends GenericFilterBean {

	@Autowired
	private JWTAuthenticateService jwtAuthenticateService;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			final String token = request.getParameter(Token.Key.value);
			
			if (!StringUtils.isEmpty(token)) {
				Authentication authentication = jwtAuthenticateService.verifyToken(token);
				if (authentication != null) {
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		}
		chain.doFilter(request, response);
	}
	
}
