package com.api.main.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.main.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {

	Optional<UserEntity> findByUserIdAndPassword(String userId, String password);

	Optional<UserEntity> findByUserId(String userId);
	
}
