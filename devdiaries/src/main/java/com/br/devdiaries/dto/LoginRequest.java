package com.br.devdiaries.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Requerimentos do usuário para fazer login")
public class LoginRequest {
    
    @Schema(description = "User's email", example = "yuri2@example.com")
    private String email;
    
    @Schema(description = "User's password", example = "yuriSenha")
    private String senha;
}
