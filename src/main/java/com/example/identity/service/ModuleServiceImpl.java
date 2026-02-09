package com.example.identity.service;

import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.identity.domain.AppModule;
import com.example.identity.domain.UserModule;
import com.example.identity.dto.CreateModuleRequest;
import com.example.identity.dto.StandardResponse;
import com.example.identity.repository.AppUserRepository;
import com.example.identity.repository.ModuleRepository;
import com.example.identity.repository.UserModuleRepository;

import jakarta.transaction.Transactional;

@Service
public class ModuleServiceImpl implements ModuleService {

	private final UserModuleRepository userModuleRepo;
	private final ModuleRepository moduleRepo;
	private final AppUserRepository userRepo;

	public ModuleServiceImpl(UserModuleRepository userModuleRepo, ModuleRepository moduleRepo,
			AppUserRepository userRepo) {
		this.userModuleRepo = userModuleRepo;
		this.moduleRepo = moduleRepo;
		this.userRepo = userRepo;
	}

	@Override
	@Transactional
	public ResponseEntity<StandardResponse> createModule(CreateModuleRequest module) {
		try {
			AppModule newModule = new AppModule();
			newModule.setCode(newModule.getCode());
			newModule.setName(newModule.getName());
			newModule.setDescription(newModule.getDescription());
			newModule.setIcon(newModule.getIcon());
			moduleRepo.save(newModule);
			return ResponseEntity.ok(new StandardResponse(HttpStatus.CREATED, "Module created"));
		} catch (DataIntegrityViolationException e) {

			return ResponseEntity.badRequest()
					.body(new StandardResponse(HttpStatus.BAD_REQUEST, "Module already existe"));

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new StandardResponse(HttpStatus.BAD_REQUEST, e.getMessage()));
		}
	}

	@Override
	@Transactional
	public ResponseEntity<StandardResponse> createSubModule(Long idParent, CreateModuleRequest subModule) {

		try {

			AppModule parent = moduleRepo.findById(idParent).orElseThrow();

			AppModule sub = new AppModule();
			sub.setCode(subModule.code());
			sub.setName(subModule.name());
			sub.setDescription(subModule.description());
			sub.setIcon(subModule.icon());
			sub.setActive(subModule.active());
			sub.setRequiredRole(subModule.requiredRole());
			sub.setRoute(subModule.route());
			sub.setParent(parent);
			moduleRepo.save(sub);

			return ResponseEntity.ok(new StandardResponse(HttpStatus.CREATED, "SubModule created"));
		} catch (NoSuchElementException e) {
			return ResponseEntity.badRequest()
					.body(new StandardResponse(HttpStatus.BAD_REQUEST, "Module Parent doesn't existe"));
		} catch (DataIntegrityViolationException e) {

			return ResponseEntity.badRequest()
					.body(new StandardResponse(HttpStatus.BAD_REQUEST, "SubModule already existe"));

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new StandardResponse(HttpStatus.BAD_REQUEST, e.getMessage()));
		}

	}

	@Override
	public ResponseEntity<StandardResponse> assignModule(Long idUser, Long idModule) {

		try {
			AppModule module = moduleRepo.findById(idModule).orElseThrow();
			userRepo.findById(idUser).orElseThrow();

			UserModule um = new UserModule();
			um.setUserId(idUser);
			um.setModule(module);

			userModuleRepo.save(um);

			return ResponseEntity.accepted().body(new StandardResponse(HttpStatus.ACCEPTED, "Module Affected to user"));
		} catch (NoSuchElementException e) {
			return ResponseEntity.badRequest()
					.body(new StandardResponse(HttpStatus.BAD_REQUEST, "Module doesn't existe"));
		} catch (DataIntegrityViolationException e) {

			return ResponseEntity.badRequest()
					.body(new StandardResponse(HttpStatus.BAD_REQUEST, "Module already affected to this user"));

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new StandardResponse(HttpStatus.BAD_REQUEST, e.getMessage()));
		}

	}

	@Override
	public ResponseEntity<StandardResponse> removeModule(Long idUser, Long idModule) {
		try {
			
			userModuleRepo.deleteByUserIdAndModuleId(idUser, idModule);
			return ResponseEntity.accepted().body(new StandardResponse(HttpStatus.ACCEPTED, "Module revoked"));
		} catch (NoSuchElementException e) {
			return ResponseEntity.badRequest()
					.body(new StandardResponse(HttpStatus.BAD_REQUEST, "Module doesn't existe"));
		} catch (DataIntegrityViolationException e) {

			return ResponseEntity.badRequest()
					.body(new StandardResponse(HttpStatus.BAD_REQUEST, "Module already affected to this user"));

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new StandardResponse(HttpStatus.BAD_REQUEST, e.getMessage()));
		}
	}

}
