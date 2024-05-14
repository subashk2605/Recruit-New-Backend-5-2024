package com.stg.recruit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stg.recruit.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	public abstract User findByEmail(String username);
	public abstract boolean existsByEmail(String email);
	

}
