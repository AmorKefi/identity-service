package com.example.identity.service;

import org.springframework.http.ResponseEntity;

import com.example.identity.dto.CreateUserRequest;
import com.example.identity.dto.StandardResponse;

public interface UserService {
	ResponseEntity<StandardResponse> createAgencyAdmin(CreateUserRequest user, String codeAgence);

	ResponseEntity<StandardResponse> createAgencyAgent(CreateUserRequest user, String codeAgence);

	ResponseEntity<?> getUserInfo(String keycloakSub);

}
