package com.example.identity.dto;

public record CreateModuleRequest(String code, String name, String description, String icon, boolean active,
		String route, String requiredRole) {

}
