package com.example.identity.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.identity.dto.LoginRequest;
import com.example.identity.dto.StandardResponse;
import com.example.identity.service.authService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final authService authService;

	@PostMapping("/login")
	public ResponseEntity<StandardResponse> login(@RequestBody LoginRequest req, HttpServletResponse response) {
		return authService.login(req, response);
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletResponse response) {
		return authService.logout(response);
	}
}
