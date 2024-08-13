package com.tapso.requestdto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthenticationRequest {
    @NotBlank
    private String emailormobilenumber;

    @NotBlank
    private String password;

	
}