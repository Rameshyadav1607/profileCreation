package com.tapso.service;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tapso.model.User;
import com.tapso.repository.UserRepository;
@Service
public class CustomUserDetailsService implements UserDetailsService{
	  private UserRepository userRepository;

	    public CustomUserDetailsService(UserRepository userRepository) {
	        this.userRepository = userRepository;
	        }

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		 User user = userRepository.findByEmail(username);
	        if (user == null) {
	            user = userRepository.findByMobileNumber(Long.parseLong(username));
	        }

	        if (user == null) {
	            throw new UsernameNotFoundException("User not found with email or mobile number: " + username);
	        }

	        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
	}

}
