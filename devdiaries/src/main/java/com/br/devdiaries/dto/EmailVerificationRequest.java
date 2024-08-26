package com.br.devdiaries.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailVerificationRequest {
    
    @Email(message = "Enter a valid email!")
    @NotBlank(message = "The email is mandatory!")
    private String email;
    
    @NotBlank(message = "The verification code is mandatory!")
    private String verificationCode;
}
