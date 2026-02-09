package com.example.identity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.identity.domain.AppModule;

public interface ModuleRepository extends JpaRepository<AppModule, Long> {
	Optional<Module> findByCode(String code);
}
