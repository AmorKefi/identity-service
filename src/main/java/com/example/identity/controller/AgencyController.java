package com.example.identity.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.identity.dto.CreateAgencyRequest;
import com.example.identity.dto.CreateUserRequest;
import com.example.identity.dto.StandardResponse;
import com.example.identity.service.AgencyService;
import com.example.identity.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/agencies")
@RequiredArgsConstructor
public class AgencyController {

	private final AgencyService agencyService;
	private final UserService userService;

	@PostMapping()
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	public ResponseEntity<StandardResponse> createAgency(@RequestBody CreateAgencyRequest agency) {
		return agencyService.createAgency(agency);
	}

	@PostMapping("{codeAgence}/admins")
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	public ResponseEntity<StandardResponse> createAgencyAdmin(@PathVariable String codeAgence,
			@RequestBody CreateUserRequest agencyAdmin) {
		return userService.createAgencyAdmin(agencyAdmin, codeAgence);
	}

	@PostMapping("{codeAgence}/agents")
	@PreAuthorize("hasAnyRole('AGENCE_ADMIN','SUPER_ADMIN')")
	public ResponseEntity<StandardResponse> createAgent(@PathVariable String codeAgence,
			@RequestBody CreateUserRequest agent) {
		return userService.createAgencyAgent(agent, codeAgence);
	}
}
