package com.example.identity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.identity.domain.Agency;

public interface AgencyRepository extends JpaRepository<Agency, Long> {
    Optional<Agency> findByCode(String code);
}
