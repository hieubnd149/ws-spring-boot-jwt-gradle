package com.cjo.starter.auth.token.impl;

import com.cjo.starter.auth.domain.Role;
import com.cjo.starter.auth.principal.UserPrincipal;
import com.cjo.starter.auth.token.AbstractRoleAuthenticatedToken;
import com.cjo.starter.auth.token.IRoleAuthenticatedTokenFactory;

public class UserAuthenticatedToken extends AbstractRoleAuthenticatedToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6729655458174068031L;

	public UserAuthenticatedToken(UserPrincipal principal) {
		super(principal);
	}

	public static class UserAuthenticatedTokenFactory implements IRoleAuthenticatedTokenFactory {

		@Override
		public AbstractRoleAuthenticatedToken getAuthenticatedToken(String id, String token) {
			return new UserAuthenticatedToken(new UserPrincipal(id, token));
		}

		@Override
		public Role getPresentedRole() {
			return Role.ROLE_USER;
		}
		
	}
	
}
