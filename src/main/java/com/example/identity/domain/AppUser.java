package com.example.identity.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "app_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true, nullable = false)
	private String keycloakSub;

	private String status;
	
	private String firstName;
	private String lastName;
	private String emailAdress;
	private String phoneNumber;
	
	public AppUser(Long id, String keycloakSub, String status) {
		super();
		this.id = id;
		this.keycloakSub = keycloakSub;
		this.status = status;
	}
	
	
}
