package com.br.devdiaries.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data

@Entity
@Table(name = "usuarios")
public class Usuario {
    
    public Usuario() {}
    
    public Usuario(Integer userId) {
        this.id = userId;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @NotBlank(message = "The name is mandatory!")
    @Size(min = 4, message = "The name must have at least 4 characters!")
    @Column(name = "nome", length = 200, nullable = false)
    private String nome;
    
    @NotBlank(message = "The username is mandatory!")
    @Size(min = 4, message = "The username must have at least 4 characters!")
    @Column(name = "username", length = 50, nullable = false)
    private String username;
    
    @Email(message = "Enter a valid email!")
    @NotBlank(message = "The email is mandatory!")
    @Column(name = "email", length = 50, nullable = false)
    private String email;
    
    @NotBlank(message = "The password is mandatory!")
    @Size(min = 8, message = "The password must have at least 8 characters!")
    @Column(name = "senha", columnDefinition = "TEXT", nullable = false)
    private String senha;
    
    @NotBlank(message = "The telephone is mandatory!")
    @Column(name = "telefone", length = 15, nullable = false)
    private String telefone;
}

