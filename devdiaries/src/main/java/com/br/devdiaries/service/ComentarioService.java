package com.br.devdiaries.service;

import com.br.devdiaries.exception.CommentNotFoundException;
import com.br.devdiaries.exception.PostNotFoundException;
import com.br.devdiaries.exception.UserNotFoundException;
import com.br.devdiaries.model.Comentario;
import com.br.devdiaries.model.Postagem;
import com.br.devdiaries.model.Usuario;
import com.br.devdiaries.repository.IComentario;
import com.br.devdiaries.repository.IPostagem;
import com.br.devdiaries.repository.IUsuario;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComentarioService {
    
    @Autowired
    private IComentario commentRepository;
    
    @Autowired
    private IPostagem postRepository;
    
    @Autowired
    private IUsuario userRepository;
    
    public Comentario criarComentario(String conteudo, Integer postId, Integer userId, Integer parentCommentId) {
        Postagem post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post not found with ID: " + postId));
        Usuario user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        Comentario parentComment = parentCommentId != null ? commentRepository.findById(parentCommentId).orElse(null) : null;
        
        Comentario comentario = new Comentario();
        comentario.setConteudo(conteudo);
        comentario.setPost(post);
        comentario.setUser(user);
        comentario.setParentComment(parentComment);
        
        comentario.setComentado_Em(LocalDateTime.now());
        return commentRepository.save(comentario);
    }
    
    public Optional<Comentario> getComentario(Integer id) {
        return commentRepository.findById(id);
    }
    
    public List<Comentario> findAll() {
        return commentRepository.findAll();
    }
    
    public Comentario editarComentario(Comentario comentario) {
        if (!commentRepository.existsById(comentario.getId())) {
            throw new CommentNotFoundException("Comment not found with Id: " + comentario.getId());
        }
        return commentRepository.save(comentario);
    }
    
    public Boolean deletarComentario(Integer id) {
        if (!commentRepository.existsById(id)) {
            throw new CommentNotFoundException("Comment not found with ID: "+ id);
        }
        commentRepository.deleteById(id);
        return true;
    }
}
