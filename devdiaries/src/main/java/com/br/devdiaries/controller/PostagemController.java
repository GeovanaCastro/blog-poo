package com.br.devdiaries.controller;

import com.br.devdiaries.dto.UserRequest;
import com.br.devdiaries.exception.PostNotFoundException;
import com.br.devdiaries.model.Postagem;
import com.br.devdiaries.service.PostagemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/posts")
public class PostagemController {
    
    @Autowired
    private PostagemService postService;
    
    //Endpoint de listagem de todas as postagens
    @GetMapping
    public ResponseEntity<List<Postagem>> getAllPosts() {
        try {
            List<Postagem> posts = postService.findAll();
            return ResponseEntity.status(200).body(posts);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    
    //Endpoint de procura de postagem pelo título
    @GetMapping("/titulo/{titulo}")
    public  ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo) {
        try {
            List<Postagem> posts = postService.findByTitulo(titulo);
            return ResponseEntity.status(200).body(posts);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    
    //Endpoint de procura de postagem pela(s) tag(s)
    @GetMapping("/tag/{tag}")
    public ResponseEntity<List<Postagem>> getByTag (@PathVariable String nomeTag) {
        try {
            List<Postagem> posts = postService.findByTags_Nome(nomeTag);
            return ResponseEntity.status(200).body(posts);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    
    @Operation(
            summary = "Create a new post",
            description = "Creates a new post with the provided details",
            responses = {
                @ApiResponse(
                responseCode = "201",
                description = "Post created successfully",
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
                description = "Post not found",
                content = @Content (mediaType = "application/json")
            )
        }
    )
    //Endpoint de criação de postagem
    @PostMapping
    public ResponseEntity<?> criarPost(@Valid @RequestBody Postagem postagem) {
        try {
            Postagem resultado = postService.criarPostagem(postagem);
            return ResponseEntity.status(201).body(resultado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
    
    //Endpoint de curtir a postagem
    @PostMapping("/{postId}/curtir")
    public ResponseEntity<?> curtirPostagem(@PathVariable Integer postId) {
        try {
            Postagem postagem = postService.curtirPostagem(postId);
            return ResponseEntity.status(200).body(postagem);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
    
    //Endpoint de descurtir a postagem
    @PostMapping("/{postId}/descurtir")
    public ResponseEntity<?> descurtirPostagem(@PathVariable Integer postId) {
        try {
            Postagem postagem = postService.descurtirPostagem(postId);
            return ResponseEntity.status(200).body(postagem);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
    
    //Endpoint de edição de postagem
    @PutMapping
    public ResponseEntity<?> editarPost(@Valid @RequestBody Postagem postagem) {
        try {
            Postagem resultado = postService.editarPostagem(postagem);
            return ResponseEntity.status(200).body(resultado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
    
    //Endpoint de deletar postagem
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarPost(@PathVariable Integer id) {
        try {
            Boolean resultado = postService.deletarPostagem(id);
            return ResponseEntity.status(204).body(resultado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
    
    // Tratamento de exceções 400 BAD REQUEST
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
    
    // Tratamento de exceções 404 NOT FOUND
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<String> handlePostNotFoundException(PostNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    
    // Tratamento de exceções genéricas (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericExceptions(Exception ex) {
        return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
 }
