package com.example.identity.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.identity.dto.KeycloakTokenResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KeycloakAuthClient {

	private final WebClient webClient;

	@Value("${keycloak.url}")
	private String url;
	@Value("${keycloak.realm}")
	private String realm;
	@Value("${keycloak.auth-client.client-id}")
	private String clientId;
	@Value("${keycloak.auth-client.client-secret}")
	private String secret;

	public String authenticate(String username, String password) {

		KeycloakTokenResponse res = webClient.post().uri(url + "/realms/" + realm + "/protocol/openid-connect/token")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters.fromFormData("grant_type", "password").with("client_id", clientId)
						.with("client_secret", secret).with("username", username).with("password", password))
				.retrieve().bodyToMono(KeycloakTokenResponse.class).block();

		return res.getAccessToken();
	}
}
