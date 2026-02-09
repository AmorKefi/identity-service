package com.example.identity.service;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.example.identity.domain.Agency;
import com.example.identity.domain.AppUser;
import com.example.identity.domain.UserAgency;
import com.example.identity.dto.LoginRequest;
import com.example.identity.dto.StandardResponse;
import com.example.identity.repository.AgencyRepository;
import com.example.identity.repository.AppUserRepository;
import com.example.identity.repository.UserAgencyRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class authService {

	private final KeycloakAuthClient kc;
	private final AppUserRepository userRepo;
	private final AgencyRepository agencyRepo;
	private final UserAgencyRepository userAgencyRepo;
	private final JwtService jwt;

	public ResponseEntity<StandardResponse> login(LoginRequest req, HttpServletResponse response) {
		try {
			String kcToken = kc.authenticate(req.getUsername(), req.getPassword());
			Claims claims = Jwts.parserBuilder().build()
					.parseClaimsJwt(kcToken.substring(0, kcToken.lastIndexOf('.') + 1)).getBody();

			String sub = claims.getSubject();
			AppUser user = userRepo.findByKeycloakSub(sub).orElseThrow();
			String generatedJwt;
			if (req.getCodeAgence() == null) {
				generatedJwt = jwt.generate(user, null, "SUPER_ADMIN");
			} else {

				Agency agency = agencyRepo.findByCode(req.getCodeAgence()).orElseThrow();
				UserAgency ua = userAgencyRepo.findByUserAndAgency(user, agency).orElseThrow();
				generatedJwt = jwt.generate(user, agency, ua.getRole());
			}

			ResponseCookie cookie = ResponseCookie.from("AUTH_TOKEN", generatedJwt).httpOnly(true).secure(true)
					.sameSite("Strict").path("/").maxAge(Duration.ofHours(1)).build();

			response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
			return ResponseEntity.ok(new StandardResponse(HttpStatus.OK, "Login Success"));
		} catch (WebClientResponseException.Unauthorized e) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new StandardResponse(HttpStatus.UNAUTHORIZED, "Login ou mot de passe incorrect"));

		} catch (WebClientResponseException.BadRequest e) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new StandardResponse(HttpStatus.UNAUTHORIZED, "Compte non activé ou action requise"));

		} catch (WebClientResponseException e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new StandardResponse(HttpStatus.UNAUTHORIZED, "Erreur d’authentification"));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new StandardResponse(HttpStatus.UNAUTHORIZED, e.getMessage()));
		}

	}

	public ResponseEntity<Void> logout(HttpServletResponse response) {
		ResponseCookie cookie = ResponseCookie.from("AUTH_TOKEN", "").httpOnly(true).secure(true).sameSite("Strict")
				.path("/").maxAge(0).build();

		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

		return ResponseEntity.ok().build();
	}
}
