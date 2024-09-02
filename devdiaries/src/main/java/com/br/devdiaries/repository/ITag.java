package com.br.devdiaries.repository;

import com.br.devdiaries.model.Tag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITag extends JpaRepository<Tag, Integer> {
     Optional<Tag> findByNome(String nome);
}
