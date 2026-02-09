package com.example.identity.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.identity.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/profil")
@RequiredArgsConstructor
public class ProfilController {

	private final UserService userService;

	@GetMapping("/me")
	public ResponseEntity<?> getUserInfo(Principal principal) {

		return this.userService.getUserInfo(principal.getName());
	}

}
