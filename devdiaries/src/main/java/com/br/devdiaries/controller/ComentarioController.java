package com.br.devdiaries.controller;

import com.br.devdiaries.dto.CommentRequest;
import com.br.devdiaries.exception.CommentNotFoundException;
import com.br.devdiaries.exception.PostNotFoundException;
import com.br.devdiaries.exception.UserNotFoundException;
import com.br.devdiaries.model.Comentario;
import com.br.devdiaries.service.ComentarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/comentarios")
public class ComentarioController {
    
    @Autowired
    private ComentarioService commentService;
    
    @Operation (
            summary = "Create a new comment",
            description = "Creates a new comment with the provided details",
            responses = {
                @ApiResponse(
                responseCode = "201",
                description = "Comment created successfully",
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
                description = "Comment not found",
                content = @Content (mediaType = "application/json")
            )
        }
    )
    //Endpoint de criação de comentário
    @PostMapping
    public ResponseEntity<Comentario> criarComentario(@RequestBody CommentRequest commentRequest) {
        Comentario comentario = commentService.criarComentario(commentRequest.getConteudo(),
                                                               commentRequest.getPostId(),
                                                               commentRequest.getUserId(),
                                                               commentRequest.getParentCommentId());
        return new ResponseEntity<>(comentario, HttpStatus.CREATED);
    }
    
    //Endpoint de listagem de comentários
    @GetMapping
    public ResponseEntity<List<Comentario>> getComentarios() {
        List<Comentario> comentarios = commentService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(comentarios);
    }
    
    @Operation (
            summary = "Edit a comment",
            description = "Edits a comment with the provided details",
            responses = {
                @ApiResponse(
                responseCode = "200",
                description = "Comment edited successfully",
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
                description = "Comment not found",
                content = @Content (mediaType = "application/json")
            )
        }
    )
    //Endpoint de editar comentário
    @PutMapping
    public ResponseEntity<Comentario> editarComentario(@RequestBody Comentario comentario) {
        Comentario comentarioEditado = commentService.editarComentario(comentario);
        return ResponseEntity.status(HttpStatus.OK).body(comentarioEditado);
    }
    
    //Endpoint de deletar comentário
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarComentario(@PathVariable Integer id) {
         commentService.deletarComentario(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
    // Tratamento de exceções para Not Found (404)
    @ExceptionHandler({CommentNotFoundException.class, PostNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<String> handleNotFoundExceptions(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Tratamento de exceções para Bad Request (400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        return new ResponseEntity<>("Invalid input: " + errorMessage, HttpStatus.BAD_REQUEST);
    }

    // Tratamento de exceções genéricas (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericExceptions(Exception ex) {
        return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
