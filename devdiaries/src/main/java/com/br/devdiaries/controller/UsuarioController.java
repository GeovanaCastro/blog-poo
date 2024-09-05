package com.br.devdiaries.controller;

import com.br.devdiaries.dto.EmailVerificationRequest;
import com.br.devdiaries.dto.LoginRequest;
import com.br.devdiaries.dto.ResetPasswordRequest;
import com.br.devdiaries.dto.UserRequest;
import com.br.devdiaries.model.RevokedToken;
import com.br.devdiaries.model.Usuario;
import com.br.devdiaries.repository.IRevokedToken;
import com.br.devdiaries.service.UsuarioService;
import com.br.devdiaries.service.JwtService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@CrossOrigin("*")
@RequestMapping("/usuarios")
public class UsuarioController {
    
    @Autowired
    IRevokedToken revokedTokenRepository;

    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    public UsuarioController(UsuarioService usuarioService, JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
    }
    
    //Endpoint de listar usu�rios
    @GetMapping
    public ResponseEntity<List<Usuario>> listaUsuarios() {
        return ResponseEntity.status(200).body(usuarioService.listarUsuario());
    }

    @Operation(
        summary = "Create a new user",
        description = "Creates a new user with the provided details",
        requestBody = @RequestBody(
            description = "User details",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserRequest.class),
                examples = {
                    @ExampleObject(
                        name = "User Example",
                        summary = "Example of a new user",
                        value = "{ \"nome\": \"Yuri Holanda\", \"username\": \"yuri_123\", \"email\": \"yuri2@example.com\", \"senha\": \"yuriSenha\" }"
                    )
                }
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "User created successfully",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid input",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = @Content (mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = @Content (mediaType = "application/json")
            )
        }
    )
    //Endpoint de cadastro de usu�rio
    @PostMapping
    public ResponseEntity<Usuario> criarUsuario(@Valid @RequestBody Usuario usuario) {
        usuarioService.enviarCodigoVerificacao(usuario);
        return ResponseEntity.status(201).build();
    }
    
    //Endpoint de verifica��o de c�digo do cadastro do usu�rio
    @PostMapping("/verificar")
    public ResponseEntity<Void> verificarCodigo(@Valid @RequestBody EmailVerificationRequest request) {
        boolean verified = usuarioService.verificarCodigo(request.getEmail(), request.getVerificationCode());
        if (verified) {
            return ResponseEntity.status(201).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Operation(
        summary = "Edit a user",
        description = "Edit a user with the provided details",
        requestBody = @RequestBody(
            description = "User details",
            required = false,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserRequest.class),
                examples = {
                    @ExampleObject(
                        name = "User Example Before Editing",
                        summary = "Example user before edit",
                        value = "{ \"nome\": \"Yuri Holanda\", \"username\": \"yuri_123\", \"email\": \"yuri2@example.com\", \"senha\": \"yuriSenha\" }"
                    ),
		    @ExampleObject(
			name = "User Example After Editing",
			summary = "Example user after edit",
			value = "{ \"nome\": \"Yuri Albuquerque\", \"username\": \"yuri_123\", \"email\": \"yuri2@example.com\", \"senha\": \"yuriSenha\" }"
                    )
                }
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "User edited successfully",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid input",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = @Content (mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = @Content (mediaType = "application/json")
            )
        }
    )
    //Endpoint de edi��o de usu�rio
    @PutMapping
    public ResponseEntity<Usuario> editarUsuario(@Valid @RequestBody Usuario usuario) {
        return ResponseEntity.status(200).body(usuarioService.editarUsuario(usuario));
    }

    //Endpoint de deletar usu�rio
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirUsuario(@PathVariable Integer id) {
        usuarioService.excluirUsuario(id);
        return ResponseEntity.status(204).build();
    }

    @Operation (
        summary = "User Login",
        description = "User requirements for login",
        requestBody = @RequestBody (
            description = "User requirements for login",
            required = true,
            content = @Content (
                mediaType = "application/json",
                schema = @Schema(implementation = LoginRequest.class),
                examples = {
                    @ExampleObject (
                        name = "Login Example",
                        summary = "User requirements for login",
                        value = "{ \"email\": \"yuri2@example.com\", \"senha\": \"yuriSenha\" }"
                    )
                }
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "User logged successfully",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid input",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = @Content (mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "404",
                description = "User don't exists",
                content = @Content (mediaType = "application/json")
            )
        }
    )
    //Endpoint de login do usu�rio
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody Map<String, String> loginRequest) {
    String email = loginRequest.get("email");
    String senha = loginRequest.get("senha");
    
    Boolean valid = usuarioService.validarSenha(email, senha);
    if (!valid) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    
    String token = jwtService.generateToken(email);
    Map<String, String> response = new HashMap<>();
    response.put("token", token);
    
    return ResponseEntity.ok(response);
}
    @Operation (
        summary = "Reset password",
        description = "User requirement for reset password",
        requestBody = @RequestBody (
            description = "User requirement for reset password",
            required = true,
            content = @Content (
                mediaType = "application/json",
                schema = @Schema(implementation = ResetPasswordRequest.class),
                examples = {
                    @ExampleObject (
                        name = "User Reset Password",
                        summary = "User requirement for reset password",
                        value = "{ \"email\": \"yuri2@example.com\" }"
                    )
                }
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Email send successfully",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid email",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = @Content (mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "404",
                description = "No users with that email found",
                content = @Content (mediaType = "application/json")
            )
        }
    )
    //Endpoint de quando o usu�rio esquecer a senha
    @PostMapping("/esqueci-senha")
    public ResponseEntity<Void> solicitarRedefinicaoSenha(@RequestBody Map<String, String> request) {
    String email = request.get("email");
    usuarioService.solicitarRedefinicaoSenha(email);
    return ResponseEntity.ok().build();
}

    //Endpoint de redefini��o de senha
    @PostMapping("/redefinir-senha")
    public ResponseEntity<Void> redefinirSenha(@RequestBody Map<String, String> request) {
    String token = request.get("token");
    String novaSenha = request.get("novaSenha");
    usuarioService.redefinirSenha(token, novaSenha);
    return ResponseEntity.ok().build();
}
    
    //Endpoint de logout do usu�rio
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = jwtService.extractTokenFromRequest(request);
    
        if (token == null) {
            return ResponseEntity.badRequest().body("Token not found.");
        }
    
        // Extrair o username do token
        String username = jwtService.extractUsername(token);
    
        if (jwtService.validateToken(token, username)) {
            // Marcar o token como revogado no banco de dados
            usuarioService.revokeToken(token);
            return ResponseEntity.ok("Logout succesfull.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
        }
    }

    // Tratamento de exce��es 404 NOT FOUND
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
        public Map<String, String> handleEntityNotFoundException(EntityNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return error;
}

    // Tratamento de exce��es 400 BAD REQUEST
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
        public Map<String, String> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return error;
}

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
    
    // Tratamento de exce��es gen�ricas (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericExceptions(Exception ex) {
        return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
