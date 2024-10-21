package com.Wallet.Application.module.security.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;

public class TokenUtil {
	private static final String SECRET_KEY="33743677397A24432646294A404D635166546A576E5A7234753778214125442A"; //WnZr4u7x

	public static String extractEmail(HttpServletRequest request) {
		
		String token = request.getHeader("Authorization").substring(7);
	    try {
	        Claims claims = Jwts.parser()
	                            .setSigningKey(SECRET_KEY)
	                            .parseClaimsJws(token)
	                            .getBody();
	        return claims.get("sub", String.class);
	    } catch (JwtException e) {
	        System.err.println("Failed to extract email: " + e.getMessage());
	        return null;
	    } catch (Exception e) {
	        System.err.println("An error occurred: " + e.getMessage());
	        return null;
	    }
	}


}
