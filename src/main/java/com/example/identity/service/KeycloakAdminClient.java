package com.example.identity.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.identity.dto.CreateUserRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KeycloakAdminClient {

	private final WebClient webClient;

	@Value("${keycloak.url}")
	private String url;
	@Value("${keycloak.realm}")
	private String realm;
	@Value("${keycloak.admin-client.client-id}")
	private String clientId;
	@Value("${keycloak.admin-client.client-secret}")
	private String secret;

	private String adminToken() {
		return webClient.post().uri(url + "/realms/" + realm + "/protocol/openid-connect/token")
				.body(BodyInserters.fromFormData("grant_type", "client_credentials").with("client_id", clientId)
						.with("client_secret", secret))
				.retrieve().bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
				}).map(r -> r.get("access_token").toString()).block();
	}

	public String createUser(CreateUserRequest req, String role) {
		String token = adminToken();

		Map<String, Object> payload = Map.of("username", req.getUsername(), "email", req.getEmail(), "firstName",
				req.getFirstName(), "lastName", req.getLastName(), "enabled", true, "emailVerified", true,
				"requiredActions", List.of(), "credentials",
				List.of(Map.of("type", "password", "value", req.getPassword(), "temporary", false)));

		webClient.post().uri(url + "/admin/realms/" + realm + "/users").header("Authorization", "Bearer " + token)
				.bodyValue(payload).retrieve().toBodilessEntity().block();

		String userId = findUserId(req.getUsername(), token);
		assignRole(userId, role, token);
		return userId;
	}

	private String findUserId(String username, String token) {

		List<Map<String, Object>> users = webClient.get()
				.uri(url + "/admin/realms/" + realm + "/users?username=" + username)
				.header("Authorization", "Bearer " + token).retrieve()
				.bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {
				}).block();

		if (users == null || users.isEmpty()) {
			throw new IllegalStateException("User not found in Keycloak");
		}

		return users.get(0).get("id").toString();
	}

	private void assignRole(String userId, String role, String token) {
		Map<String, Object> roleRep = webClient.get().uri(url + "/admin/realms/" + realm + "/roles/" + role)
				.header("Authorization", "Bearer " + token).retrieve()
				.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
				}).block();

		webClient.post().uri(url + "/admin/realms/" + realm + "/users/" + userId + "/role-mappings/realm")
				.header("Authorization", "Bearer " + token).bodyValue(List.of(roleRep)).retrieve().toBodilessEntity()
				.block();
	}
}
