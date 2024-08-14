package com.tapso.requestdto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AuthenticationResponse {
	
	private String jwt;
	private String username;
    private String emailOrMobileNumber;
    private Collection<? extends GrantedAuthority> authorities;
	
    public AuthenticationResponse(String jwt, UserDetails userDetails) {
        this.jwt = jwt;
        this.username = userDetails.getUsername();
        this.emailOrMobileNumber = userDetails.getUsername(); // Assuming username is either email or mobile number
        this.authorities = userDetails.getAuthorities();
    }

	

	
}
