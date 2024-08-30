package com.br.devdiaries.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "User requirement for reset password")
public class ResetPasswordRequest {
    
    @Schema(description = "User email for reset password", example = "yuri2@example.com")
    private String email;
}
