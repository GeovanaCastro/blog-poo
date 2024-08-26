package com.br.devdiaries.controller;

import com.br.devdiaries.model.Postagem;
import com.br.devdiaries.repository.IPostagem;
import com.br.devdiaries.service.PostagemService;
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
    
    @GetMapping
    public ResponseEntity<List<Postagem>> getAllPosts() {
        List<Postagem> posts = postService.findAll();
        return ResponseEntity.status(200).body(posts);
    }
    
    @GetMapping("/titulo/{titulo}")
    public  ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo) {
        List<Postagem> posts = postService.findByTitulo(titulo);
        return ResponseEntity.status(200).body(posts);
    }
    
    @PostMapping
    public ResponseEntity<Postagem> criarPost(@Valid @RequestBody Postagem postagem) {
        Postagem resultado = postService.criarPostagem(postagem);
        return ResponseEntity.status(201).body(resultado);
    }
    
    @PostMapping("/{postId}/curtir")
    public ResponseEntity<?> curtirPostagem(@PathVariable Integer postId) {
        Postagem postagem = postService.curtirPostagem(postId);
        return ResponseEntity.status(200).body(postagem);
    }
    
    @PostMapping("/{postId}/descurtir")
    public ResponseEntity<?> descurtirPostagem(@PathVariable Integer postId) {
        Postagem postagem = postService.descurtirPostagem(postId);
        return ResponseEntity.status(200).body(postagem);
    }
    
    @PutMapping
    public ResponseEntity<Postagem> editarPost(@Valid @RequestBody Postagem postagem) {
        Postagem resultado = postService.editarPostagem(postagem);
        return ResponseEntity.status(200).body(resultado);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarPost(@PathVariable Integer id) {
        Boolean resultado = postService.deletarPostagem(id);
        return ResponseEntity.status(204).body(resultado);
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
 }
