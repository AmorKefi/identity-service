package com.example.identity.dto;

import java.util.List;

import com.example.identity.domain.AppModule;
import com.example.identity.domain.AppUser;

public record UserProfilResponse(String firstName, String lastName, String email, String phoneNumber,
		List<ModuleResponse> modules) {

	public record ModuleResponse(String code, String name, String description, String icon, String route,
			List<ModuleResponse> subModules) {

		public static ModuleResponse from(AppModule module) {
			return new ModuleResponse(module.getCode(), module.getName(), module.getDescription(), module.getIcon(),
					module.getRoute(),
					module.getSubModules().stream().map(UserProfilResponse.ModuleResponse::from).toList());
		}
	}

	public static UserProfilResponse from(AppUser user) {
		return new UserProfilResponse(user.getFirstName(), user.getLastName(), user.getEmailAdress(),
				user.getPhoneNumber(), null);
	}

}
