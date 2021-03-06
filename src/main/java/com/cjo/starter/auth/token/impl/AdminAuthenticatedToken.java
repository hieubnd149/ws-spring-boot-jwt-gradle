package com.cjo.starter.auth.token.impl;

import com.cjo.starter.auth.domain.Role;
import com.cjo.starter.auth.principal.AdminPrincipal;
import com.cjo.starter.auth.token.AbstractRoleAuthenticatedToken;
import com.cjo.starter.auth.token.IRoleAuthenticatedTokenFactory;

public class AdminAuthenticatedToken extends AbstractRoleAuthenticatedToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = -309211816317092680L;

	public AdminAuthenticatedToken(AdminPrincipal principal) {
		super(principal);
	}

	public static class AdminAuthenticatedTokenFactory implements IRoleAuthenticatedTokenFactory {

		@Override
		public AbstractRoleAuthenticatedToken getAuthenticatedToken(final String id, final String token) {
			return new AdminAuthenticatedToken(new AdminPrincipal(id, token));
		}

		@Override
		public Role getPresentedRole() {
			return Role.ROLE_ADMIN;
		}
		
	}
	
}
