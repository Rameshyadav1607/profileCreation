package com.tapso.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tapso.model.Otp;

public interface OtpRepository extends JpaRepository<Otp,Long>{

	 @Query("SELECT o FROM Otp o WHERE o.emailOrMobile = :emailOrMobile AND o.otp = :otp")
	    Optional<Otp> findByEmailOrMobileAndOtp(@Param("emailOrMobile") String emailOrMobile, @Param("otp") String otp);
	

}
