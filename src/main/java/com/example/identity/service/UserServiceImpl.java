package com.example.identity.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.identity.domain.Agency;
import com.example.identity.domain.AppUser;
import com.example.identity.domain.UserAgency;
import com.example.identity.dto.CreateUserRequest;
import com.example.identity.dto.StandardResponse;
import com.example.identity.dto.UserProfilResponse;
import com.example.identity.dto.UserProfilResponse.ModuleResponse;
import com.example.identity.repository.AgencyRepository;
import com.example.identity.repository.AppUserRepository;
import com.example.identity.repository.UserAgencyRepository;
import com.example.identity.repository.UserModuleRepository;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

	private final KeycloakAdminClient kcAdmin;
	private final AgencyRepository agencyRepo;
	private final AppUserRepository userRepo;
	private final UserAgencyRepository uaRepo;
	private final UserModuleRepository userModuleRepo;

	public UserServiceImpl(KeycloakAdminClient kcAdmin, AgencyRepository agencyRepo, AppUserRepository userRepo,
			UserAgencyRepository uaRepo, UserModuleRepository userModuleRepo) {
		this.kcAdmin = kcAdmin;
		this.agencyRepo = agencyRepo;
		this.userRepo = userRepo;
		this.uaRepo = uaRepo;
		this.userModuleRepo = userModuleRepo;
	}

	@Override
	@Transactional
	public ResponseEntity<StandardResponse> createAgencyAdmin(CreateUserRequest user, String codeAgence) {
		try {

			String sub = kcAdmin.createUser(user, "AGENCE_ADMIN");
			AppUser newUser = userRepo.save(new AppUser(null, sub, "ACTIVE", user.getFirstName(), user.getLastName(),
					user.getEmail(), user.getPhoneNumber()));

			Agency agency = agencyRepo.findByCode(codeAgence).orElseThrow();

			uaRepo.save(new UserAgency(null, newUser, agency, "AGENCE_ADMIN"));

			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new StandardResponse(HttpStatus.CREATED, "agency admin created"));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new StandardResponse(HttpStatus.BAD_REQUEST, e.getMessage()));
		}

	}

	@Override
	public ResponseEntity<StandardResponse> createAgencyAgent(CreateUserRequest user, String codeAgence) {
		try {

			String sub = kcAdmin.createUser(user, "AGENT");
			AppUser newUser = userRepo.save(new AppUser(null, sub, "ACTIVE", user.getFirstName(), user.getLastName(),
					user.getEmail(), user.getPhoneNumber()));
			Agency agency = agencyRepo.findByCode(codeAgence).orElseThrow();

			uaRepo.save(new UserAgency(null, newUser, agency, "AGENT"));
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new StandardResponse(HttpStatus.CREATED, "agency agent created"));
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.badRequest().body(new StandardResponse(HttpStatus.BAD_REQUEST, e.getMessage()));
		}

	}

	@Override
	public ResponseEntity<?> getUserInfo(String keycloakSub) {
		try {
			AppUser currentUser = this.userRepo.findByKeycloakSub(keycloakSub).orElseThrow();
			List<ModuleResponse> modules = this.userModuleRepo.findByUserId(currentUser.getId()).stream()
					.map(um -> ModuleResponse.from(um.getModule())).toList();
			UserProfilResponse userInfo = new UserProfilResponse(currentUser.getFirstName(), currentUser.getLastName(),
					currentUser.getEmailAdress(), currentUser.getPhoneNumber(), modules);
			return ResponseEntity.ok(userInfo);
		} catch (NoSuchElementException e) {
			return ResponseEntity.badRequest()
					.body(new StandardResponse(HttpStatus.BAD_REQUEST, "User doesn't existe"));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new StandardResponse(HttpStatus.BAD_REQUEST, e.getMessage()));
		}

	}

}
