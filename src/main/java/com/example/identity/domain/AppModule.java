package com.example.identity.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "modules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppModule {

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true)
	private String code;
	private String name;
	private String description;
	private String icon;
	private String route;
	private String requiredRole;

	@ManyToOne
	private AppModule parent;

	private boolean active;

	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
	private List<AppModule> subModules = new ArrayList<>();

}