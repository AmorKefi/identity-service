package com.example.identity.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAgency {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private AppUser user;

    @ManyToOne
    private Agency agency;

    private String role;
}
