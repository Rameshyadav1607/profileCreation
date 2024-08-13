package com.tapso.requestdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordResetRequest {
    
    @NotBlank
    private String emailOrMobile;

    @NotBlank
    private String otp;

    @NotBlank
    @Size(min = 10)
    private String newPassword;

    @NotBlank
    @Size(min = 10)
    private String confirmPassword;
}