package com.tapso.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tapso.model.User;

public interface UserRepository extends JpaRepository<User,Long>{


	boolean existsByMobileNumber(Long mobileNumber);

	boolean existsByEmail(String email);

	User findByEmail(String email);

	User findByMobileNumber(long mobileNumber);
	
}
