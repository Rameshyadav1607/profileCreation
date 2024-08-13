package com.tapso.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tapso.exception.DuplicateEmailException;
import com.tapso.exception.DuplicateMobileNumberException;
import com.tapso.exception.UserNotFoundException;
import com.tapso.model.Otp;
import com.tapso.model.User;
import com.tapso.repository.OtpRepository;
import com.tapso.repository.UserRepository;
import com.tapso.requestdto.PasswordResetRequest;
import com.tapso.requestdto.RegisterRequest;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private  UserRepository userRepository;
    private  PasswordEncoder passwordEncoder;
    private  EmailService emailService;
    private OtpRepository otpRepository;
//    private SmsService smsService;

	

	public ResponseEntity<?> createProfile(RegisterRequest registerRequest) {
    	 try {
             // Map the RegisterRequest to User entity
             ModelMapper mapper = new ModelMapper();
             User user = mapper.map(registerRequest, User.class);
             user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

             // Check if the email already exists
             if (userRepository.existsByEmail(registerRequest.getEmail())) {
                 throw new DuplicateEmailException("Email address is already in use.");
             }

             // Check if the mobile number already exists
             if (userRepository.existsByMobileNumber(registerRequest.getMobileNumber())) {
                 throw new DuplicateMobileNumberException("Mobile number is already in use.");
             }

          // Save the user if both email and mobile number are unique
             User savedUser = userRepository.save(user);

             // Send registration email
             String subject = "Registration Successful";
             String body = "Dear " + savedUser.getUserName() + ",\n\n" +
                           "Thank you for registering. Your registration was successful.\n\n" +
                           "Best regards,\n" +
                           "Your Company";
             emailService.sendRegistrationEmail(savedUser.getEmail(), subject, body);

             // Return the email and a success message
             return ResponseEntity.status(HttpStatus.CREATED)
                                  .body("User profile created successfully. An email has been sent to: " + savedUser.getEmail());
         } catch (DuplicateEmailException | DuplicateMobileNumberException e) {
             // Handle specific validation exceptions
            // logger.error("Validation error: ", e);
             return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
         } catch (Exception e) {
             // Handle general exceptions
             logger.error("Error creating user profile: ", e);
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
         }
     }
	
	 public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService,
			OtpRepository otpRepository) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.emailService = emailService;
		this.otpRepository = otpRepository;
	}

	public ResponseEntity<?> resetpassword(String email, Long mobilenumber) {
	        User user = null;

	        // Check if email is provided
	        if (email != null && !email.isEmpty()) {
	            user = userRepository.findByEmail(email);
	            if (user == null) {
	                return ResponseEntity.status(404).body("Email does not exist.");
	            }
	            return generateAndSendOtp(user, email, false); // Email case
	        }

	        // Check if mobile number is provided
	        if (mobilenumber != null) {  
	            user = userRepository.findByMobileNumber(mobilenumber);
	            if (user == null) {
	                return ResponseEntity.status(404).body("Mobile number does not exist.");
	            }
	            return generateAndSendOtp(user, String.valueOf(mobilenumber), true); // Mobile number case
	        }

	        return ResponseEntity.badRequest().body("Please provide either email or mobile number.");
	    }

	    private ResponseEntity<?> generateAndSendOtp(User user, String identifier, boolean isMobileNumber) {
	        String otp = generateOtp();
	        LocalDateTime expiration = LocalDateTime.now().plusMinutes(10);

	        Otp otpEntity = new Otp();
	        otpEntity.setOtp(otp);
	        otpEntity.setEmailOrMobile(identifier);
	        otpEntity.setExpiryDate(expiration);
	        otpEntity.setUser(user);

	        otpRepository.save(otpEntity);

	        // Send OTP via email or SMS
	        if (isMobileNumber) {
	            // Send OTP via SMS
	            //smsService.sendSms(identifier, "Your OTP code is: " + otp);
	        	String message="Dear customer :";
	        //	smsService.sendSms(identifier, otp);
	        } else {
	            // Send OTP via Email
	            emailService.sendOtpEmail(user.getEmail(), "Your OTP Code", "Your OTP code is: " + otp);
	        }

	        return ResponseEntity.ok("OTP sent successfully to " + identifier);
	    }

	    private String generateOtp() {
	        return String.format("%06d", new Random().nextInt(999999));
	    }

		public ResponseEntity<?> verifyandRestPassword(PasswordResetRequest passwordResetRequest) {
			//validate new password and conform password match
			if(!passwordResetRequest.getNewPassword().equals(passwordResetRequest.getConfirmPassword())) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("new pass and conform password must be same");
			}
			 // Retrieve the OTP entity
		    Optional<Otp> otpEntityOpt = otpRepository.findByEmailOrMobileAndOtp(passwordResetRequest.getEmailOrMobile(), passwordResetRequest.getOtp());

		  if (!otpEntityOpt.isPresent()) {
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP.");
		    }
		  Otp otpEntity = otpEntityOpt.get();
		  // Check if the OTP is expired
		    if (otpEntity.getExpiryDate().isBefore(LocalDateTime.now())) {
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OTP has expired.");
		    }
		 // Retrieve the user associated with the OTP
		    User user = otpEntity.getUser();
		 // Encode and set the new password
		    user.setPassword(passwordEncoder.encode(passwordResetRequest.getNewPassword()));
		    // Save the updated user entity
		    userRepository.save(user);
		 // Send password update notification email
		    String subject = "Your Password Has Been Updated";
		    String body = "Dear " + user.getUserName() + ",\n\n" +
		                  "Your password has been successfully updated.\n\n" +
		                  "If you did not perform this action, please contact our support team immediately.\n\n" +
		                  "Best regards,\n" +
		                  "Your Company";
		    emailService.sendPasswordUpdateEmail(user.getEmail(), subject, body);

		return  ResponseEntity.ok("Password has been reset successfully.");
		}


		}

  
