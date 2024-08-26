package com.br.devdiaries.repository;

import com.br.devdiaries.model.Comentario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IComentario extends JpaRepository<Comentario, Integer>{
    List<Comentario> findByPostId(Integer postId);
}
