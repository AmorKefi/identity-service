package com.example.identity.service;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.identity.domain.Agency;
import com.example.identity.domain.AppUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private final Key key;

	public JwtService(@Value("${jwt.secret}") String secret) {
		this.key = Keys.hmacShaKeyFor(secret.getBytes());
	}

	public Claims parse(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

	public String generate(AppUser user, Agency agency, String role) {
		return Jwts.builder().setSubject(user.getKeycloakSub()).claim("userId", user.getId()).claim("role", role)
				.claim("agencyCode", agency != null ? agency.getCode() : null).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 3600_000)).signWith(key).compact();
	}
}
