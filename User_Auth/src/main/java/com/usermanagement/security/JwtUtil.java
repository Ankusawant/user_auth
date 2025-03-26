package com.usermanagement.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

	private final String jwtSecret;
	private final int jwtExpirationMs = 3600000;

	public JwtUtil() {
		SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
		this.jwtSecret = Base64.getEncoder().encodeToString(key.getEncoded());
	}

	public String generateJwtToken(String username, List<String> roles) {
		return Jwts.builder().setSubject(username).claim("rl", roles).setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS256, jwtSecret).compact();
	}

	public boolean validateJwtToken(String token) {
		try {
			Jwts.parser().setSigningKey(Base64.getDecoder().decode(jwtSecret)).parseClaimsJws(token);
			return true;
		} catch (JwtException e) {
			System.out.println("Invalid JWT token: " + e.getMessage());
		}
		return false;
	}

	public String extractUsername(String token) {
		return Jwts.parser().setSigningKey(Base64.getDecoder().decode(jwtSecret)).parseClaimsJws(token).getBody()
				.getSubject();
	}

	public List<String> extractRoles(String token) {
		return (List<String>) Jwts.parser().setSigningKey(Base64.getDecoder().decode(jwtSecret)).parseClaimsJws(token)
				.getBody().get("roles");
	}
}