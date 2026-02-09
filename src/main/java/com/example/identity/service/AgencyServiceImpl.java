package com.example.identity.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.identity.domain.Agency;
import com.example.identity.dto.CreateAgencyRequest;
import com.example.identity.dto.StandardResponse;
import com.example.identity.repository.AgencyRepository;

@Service
public class AgencyServiceImpl implements AgencyService {

	private final AgencyRepository agencyRepo;

	public AgencyServiceImpl(AgencyRepository agencyRepository) {
		this.agencyRepo = agencyRepository;
	}

	@Override
	public ResponseEntity<StandardResponse> createAgency(CreateAgencyRequest agency) {
		try {
			agencyRepo.save(new Agency(null, agency.getCode(), agency.getName(), agency.getStreetName(),
					agency.getCountry(), agency.getEmailAdress()));

			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new StandardResponse(HttpStatus.CREATED, "agency created"));
		}catch(DataIntegrityViolationException e) {
			return ResponseEntity.badRequest().body(new StandardResponse(HttpStatus.BAD_REQUEST, "Agency already existe"));
		}
		catch (Exception e) {
			return ResponseEntity.badRequest().body(new StandardResponse(HttpStatus.BAD_REQUEST, e.getMessage()));
		}
	}

}
