package com.br.devdiaries.repository;

import com.br.devdiaries.model.Postagem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IPostagem extends JpaRepository<Postagem, Integer>{
    List<Postagem> findAllByTituloContainingIgnoreCase(String titulo);
}
