package com.example.identity.service;

import org.springframework.http.ResponseEntity;

import com.example.identity.dto.CreateModuleRequest;
import com.example.identity.dto.StandardResponse;

public interface ModuleService {

	ResponseEntity<StandardResponse> createModule(CreateModuleRequest module);

	ResponseEntity<StandardResponse> createSubModule(Long idParent, CreateModuleRequest subModule);

	ResponseEntity<StandardResponse> assignModule(Long idUser, Long idModule);
	
	ResponseEntity<StandardResponse> removeModule(Long idUser, Long idModule);
	
	
}
