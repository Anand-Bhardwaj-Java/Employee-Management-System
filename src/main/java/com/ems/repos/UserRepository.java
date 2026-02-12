package com.ems.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.entities.UserEntity;

import java.util.Optional;


public interface UserRepository extends JpaRepository<UserEntity, Integer>{

	Optional<UserEntity> findByEmail(String email);
}
