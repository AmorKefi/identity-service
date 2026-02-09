package com.example.identity.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.identity.service.JwtService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws ServletException, IOException {
		Cookie[] cookies = req.getCookies();

		if (cookies == null) {
			chain.doFilter(req, res);
			return;
		}

		String token = Arrays.stream(cookies).filter(c -> "AUTH_TOKEN".equals(c.getName())).map(Cookie::getValue)
				.findFirst().orElse(null);

		if (token == null) {
			chain.doFilter(req, res);
			return;
		}

		Claims claims = jwtService.parse(token);

		String role = claims.get("role", String.class);

		Authentication auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
				List.of(new SimpleGrantedAuthority("ROLE_" + role)));
		SecurityContextHolder.getContext().setAuthentication(auth);
		chain.doFilter(req, res);
	}
}
