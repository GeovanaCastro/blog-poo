package com.br.devdiaries.service;

import com.br.devdiaries.model.Postagem;
import com.br.devdiaries.repository.IPostagem;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostagemService {
    
    @Autowired
    private IPostagem postRepository;
    
    public Postagem criarPostagem(Postagem postagem) {
        postagem.setCriado_Em(LocalDateTime.now());
        return postRepository.save(postagem);
    }
    
    public Postagem curtirPostagem(Integer postId) {
        Postagem postagem = postRepository.findById(postId).orElse(null);
        if (postagem != null) {
            postagem.setCurtidas(postagem.getCurtidas() + 1);
            postagem = postRepository.save(postagem);
        }
        return postagem;
    }
    
    public Postagem descurtirPostagem(Integer postId) {
        Postagem postagem = postRepository.findById(postId).orElse(null);
        if (postagem != null && postagem.getCurtidas() != 0) {
            postagem.setCurtidas(postagem.getCurtidas() - 1);
            postagem = postRepository.save(postagem);
        }
        return postagem;
    }
    
    public Postagem editarPostagem(Postagem postagem) {
        return postRepository.save(postagem);
    }
    
    public Optional<Postagem> getPostagem(Integer postId) {
        return postRepository.findById(postId);
    }
    
    public List<Postagem> findByTitulo(String titulo) {
        return postRepository.findAllByTituloContainingIgnoreCase(titulo);
    }
    
    public List<Postagem> findAll() {
        return postRepository.findAll();
    }
    
    public Boolean deletarPostagem(Integer id) {
        postRepository.deleteById(id);
        return true;
    }
}
