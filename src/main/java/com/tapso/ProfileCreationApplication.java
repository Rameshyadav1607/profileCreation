package com.tapso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tapso.jwt.JwtUtil;

@SpringBootApplication
public class ProfileCreationApplication {
	

	public static void main(String[] args) {
		SpringApplication.run(ProfileCreationApplication.class, args);
		
	}

}
