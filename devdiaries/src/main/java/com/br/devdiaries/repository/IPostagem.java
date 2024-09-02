package com.br.devdiaries.repository;

import com.br.devdiaries.model.Postagem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPostagem extends JpaRepository<Postagem, Integer>{
    List<Postagem> findAllByTituloContainingIgnoreCase(String titulo);
    List<Postagem> findByTags_Nome(String nome);
}
