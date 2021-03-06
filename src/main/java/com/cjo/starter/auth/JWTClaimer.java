package com.cjo.starter.auth;

import com.auth0.jwt.JWTCreator.Builder;
import com.cjo.starter.auth.domain.JWTPayload;

/**
 * 
 * The class JWTClaimer<br>
 * <br>
 * Jwt custom person to extract information from jwt token.<br>
 * <br>
 * @author Tomas
 * @version 1.0
 * @since Feb 20, 2019
 *
 */
public class JWTClaimer {

	public static enum ClaimContent {
		email,
		role
	}
	
	public static void claim(JWTPayload payload, Builder builder) {
		builder.withClaim(ClaimContent.email.name(), payload.getEmail());
		builder.withClaim(ClaimContent.role.name(), payload.getRole());
	}
	
}
