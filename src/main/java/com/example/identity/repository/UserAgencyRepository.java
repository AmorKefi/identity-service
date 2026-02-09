package com.example.identity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.identity.domain.Agency;
import com.example.identity.domain.AppUser;
import com.example.identity.domain.UserAgency;

public interface UserAgencyRepository extends JpaRepository<UserAgency, Long> {
	Optional<UserAgency> findByUserAndAgency(AppUser user, Agency agency);
}
