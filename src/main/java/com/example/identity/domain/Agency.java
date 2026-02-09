package com.example.identity.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agency {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String code;

    private String name;
    
    private String streetName;
    private String country;
    private String emailAdress;
	public Agency(Long id, String code, String name) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
	}
    
    
    
}
