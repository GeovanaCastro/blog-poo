package com.br.devdiaries.controller;

import com.br.devdiaries.dto.CommentRequest;
import com.br.devdiaries.exception.CommentNotFoundException;
import com.br.devdiaries.exception.PostNotFoundException;
import com.br.devdiaries.exception.UserNotFoundException;
import com.br.devdiaries.model.Comentario;
import com.br.devdiaries.service.ComentarioService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    
    @PostMapping
    public ResponseEntity<Comentario> criarComentario(@RequestBody CommentRequest commentRequest) {
        Comentario comentario = commentService.criarComentario(commentRequest.getConteudo(),
                                                               commentRequest.getPostId(),
                                                               commentRequest.getUserId(),
                                                               commentRequest.getParentCommentId());
        return new ResponseEntity<>(comentario, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<Comentario>> getComentarios() {
        List<Comentario> comentarios = commentService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(comentarios);
    }
    
    @PostMapping("/{id}/curtir")
    public ResponseEntity<Comentario> curtirComentario(@PathVariable Integer id) {
        Comentario comentarioCurtido = commentService.curtirComentario(id);
        return ResponseEntity.status(HttpStatus.OK).body(comentarioCurtido);
    }
    
    @PostMapping("/{id}/descurtir")
    public ResponseEntity<Comentario> descurtirComentario(@PathVariable Integer id) {
        Comentario comentarioDescurtido = commentService.descurtirComentario(id);
        return ResponseEntity.status(HttpStatus.OK).body(comentarioDescurtido);
    }
    
    @PutMapping
    public ResponseEntity<Comentario> editarComentario(@RequestBody Comentario comentario) {
        Comentario comentarioEditado = commentService.editarComentario(comentario);
        return ResponseEntity.status(HttpStatus.OK).body(comentarioEditado);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarComentario(@PathVariable Integer id) {
         commentService.deletarComentario(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
    @ExceptionHandler({CommentNotFoundException.class, PostNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<String> handleNotFoundExceptions(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericExceptions(Exception ex) {
        return new ResponseEntity<>("An unexpected error ocurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
