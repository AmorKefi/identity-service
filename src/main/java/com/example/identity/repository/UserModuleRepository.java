package com.example.identity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.identity.domain.UserModule;

public interface UserModuleRepository extends JpaRepository<UserModule, Long> {

	void deleteByUserIdAndModuleId(Long userId, Long moduleId);

	List<UserModule> findByUserId(Long userId);

}
