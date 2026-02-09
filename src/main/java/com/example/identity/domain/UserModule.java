package com.example.identity.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_modules", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "module_id" }))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModule {

	@Id
	@GeneratedValue
	private Long id;

	private Long userId;

	@ManyToOne
	private AppModule module;
}