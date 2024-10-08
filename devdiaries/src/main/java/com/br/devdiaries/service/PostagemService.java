package com.br.devdiaries.service;

import com.br.devdiaries.exception.PostNotFoundException;
import com.br.devdiaries.model.Postagem;
import com.br.devdiaries.model.Tag;
import com.br.devdiaries.repository.IPostagem;
import com.br.devdiaries.repository.ITag;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostagemService {
    
    @Autowired
    private IPostagem postRepository;
    
    @Autowired
    private ITag tagRepository;
    
    public Postagem criarPostagem(Postagem postagem) {
        try {
        postagem.setCriado_Em(LocalDateTime.now());
        return postRepository.save(postagem);
        } catch (Exception e) {
            throw new RuntimeException("Error creating post. Please try again later.", e);
        }
    }
    
    public Postagem curtirPostagem(Integer postId) {
        try {
        Postagem postagem = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found!"));
            postagem.setCurtidas(postagem.getCurtidas() + 1);
            return postRepository.save(postagem);
        } catch (Exception e) {
            throw new RuntimeException("Error liking post. Please try again later.", e);
        }
    }
    
    public Postagem descurtirPostagem(Integer postId) {
        try {
            Postagem postagem = postRepository.findById(postId)
                    .orElseThrow(() -> new PostNotFoundException("Post not found!"));
            if (postagem.getCurtidas() > 0) {
                postagem.setCurtidas(postagem.getCurtidas() - 1);
                return postRepository.save(postagem);
            } else {
                throw new RuntimeException("You cannot unlike a post with 0 likes.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error disliking post. Please try again later.", e);
        }
    }
    
    public Postagem editarPostagem(Postagem postagem) {
        try {
            if (postRepository.existsById(postagem.getId())) {
                return postRepository.save(postagem);
            } else {
                throw new RuntimeException("Post not found!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error editing post. Please try again later.", e);
        }
        
    }
    
    public Optional<Postagem> getPostagem(Integer postId) {
        try {
            return postRepository.findById(postId);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching post. Please try again later.", e);
        }
    }
    
    public List<Postagem> findByTitulo(String titulo) {
        try {
            return postRepository.findAllByTituloContainingIgnoreCase(titulo);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching posts by title. Please try again later.", e);
        }
    }
    
    public List<Postagem> findAll() {
        try {
            return postRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching all posts. Please try again later.", e);
        }
    }
    
     public Postagem adicionarTagAoPost(Integer postId, String nomeTag) {
        Postagem postagem = postRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException("Post not found"));

        Tag tag = tagRepository.findByNome(nomeTag)
            .orElseGet(() -> {
                Tag novaTag = new Tag();
                novaTag.setNome(nomeTag);
                return tagRepository.save(novaTag);
            });

        postagem.getTags().add(tag);
        return postRepository.save(postagem);
    }
     
     public List<Postagem> findByTags_Nome(String nomeTag) {
         try {
             return postRepository.findByTags_Nome(nomeTag);
         } catch (Exception e) {
             throw new RuntimeException("Error fetching posts by tag. Please try again later.");
         }
     }
    
    public Boolean deletarPostagem(Integer id) {
        try {
            if (postRepository.existsById(id)) {
                postRepository.deleteById(id);
                return true;
            } else {
                throw new RuntimeException("Post not found!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deleting post. Please try again later.", e);
        }
    }
}
