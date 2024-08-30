package com.br.devdiaries.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Detalhes do usuário para criação ou atualização")
public class UserRequest {
    
    @Schema(description = "User's unique identifier", example = "1")
    private Integer id;
    
     @Schema(description = "User's name", example = "Yuri Holanda")
    private String nome;

    @Schema(description = "User's username", example = "yuri_123")
    private String username;

    @Schema(description = "User's email", example = "yuri2@example.com")
    private String email;
    
    @Schema(description = "User's password", example = "yuriSenha")
    private String senha;
}
