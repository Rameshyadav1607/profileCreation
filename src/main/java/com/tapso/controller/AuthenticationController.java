package com.tapso.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tapso.jwt.JwtUtil;
import com.tapso.requestdto.AuthenticationRequest;
import com.tapso.requestdto.AuthenticationResponse;
import com.tapso.service.CustomUserDetailsService;
import com.tapso.service.EmailService;

@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private  EmailService emailService;
    
    private static final Logger logger=LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
       logger.info("reccived username and password"+ authenticationRequest.getEmailormobilenumber()+ " "+ authenticationRequest.getPassword());
    	
    	try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmailormobilenumber(), authenticationRequest.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmailormobilenumber());
        logger.info("username : received from userdetails:"+userDetails);
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());
        logger.info("jwt received "+ jwt);
     // Send login success email
        String subject = "Login Successful";
        String body = "Dear You have successfully logged in to your account.";
        emailService.sendRegistrationEmail(userDetails.getUsername(), subject, body);
        logger.info("Login success email sent to: " + userDetails.getUsername());
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwt, userDetails);
        return ResponseEntity.ok(authenticationResponse);
    }
}