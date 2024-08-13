package com.tapso.requestdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class RegisterRequest {
	    private String pic; // URL or path to the profile picture

	    @NotBlank
	    private String userName;

	    @NotBlank
	    @Email
	    private String email;

	    @NotBlank
	    @Size(min = 10)
	    private String password;
	    @NotBlank
	    private Long mobileNumber;
		
		
}