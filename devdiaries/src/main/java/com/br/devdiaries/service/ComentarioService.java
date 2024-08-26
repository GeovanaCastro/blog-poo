package com.br.devdiaries.service;

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
        Postagem post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not find"));
        Usuario user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not find"));
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
        return commentRepository.save(comentario);
    }
    
    public Comentario curtirComentario(Integer id) {
        Comentario comentario = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
        comentario.setCurtidas(comentario.getCurtidas() + 1);
        return commentRepository.save(comentario);
    }
    
    public Comentario descurtirComentario(Integer id) {
        Comentario comentario = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
        if (comentario.getCurtidas() != 0) {
            comentario.setCurtidas(comentario.getCurtidas() - 1);
        }
        return commentRepository.save(comentario);
    }
    
    public Boolean deletarComentario(Integer id) {
        commentRepository.deleteById(id);
        return true;
    }
}
