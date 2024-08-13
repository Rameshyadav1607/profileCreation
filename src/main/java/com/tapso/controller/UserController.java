package com.tapso.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tapso.exception.UserNotFoundException;
import com.tapso.requestdto.PasswordResetRequest;
import com.tapso.requestdto.RegisterRequest;
import com.tapso.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	
	private static final Logger logger=LoggerFactory.getLogger(UserController.class);
	private UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/create")
	private ResponseEntity<?> createProfile(@RequestBody RegisterRequest registerRequest){
		//logger.info("recevied email :"+ registerRequest.getEmail());
		return userService.createProfile(registerRequest);
		
	}
	@PostMapping("/resetpassword")
    public ResponseEntity<?> resetpassword(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Long mobilenumber) {
        return userService.resetpassword(email, mobilenumber);
    }
    @PostMapping("/verifyotpandRestpassword")
	public ResponseEntity<?> verifyandRestPassword(@RequestBody PasswordResetRequest passwordResetRequest){
		return userService.verifyandRestPassword(passwordResetRequest);
		
	}
}
