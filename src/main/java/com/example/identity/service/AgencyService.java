package com.example.identity.service;

import org.springframework.http.ResponseEntity;

import com.example.identity.dto.CreateAgencyRequest;
import com.example.identity.dto.StandardResponse;

public interface AgencyService {

	ResponseEntity<StandardResponse> createAgency(CreateAgencyRequest agency);

}
