package com.example.identity.dto;

import lombok.Data;

@Data
public class CreateAgencyRequest {
	private String code;
	private String name;
	private String streetName;
	private String country;
	private String emailAdress;
}