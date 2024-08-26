package com.br.devdiaries.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Data;

@Data

@Entity
@Table(name = "posts")
public class Postagem {
    
    public Postagem() {}
    
    public Postagem(Integer postId) {
        this.id = postId;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @NotBlank(message = "A title for your post is a must!")
    @Size(min = 8, message = "The title must have at least 8 characters!")
    @Column(name = "titulo", length = 100, nullable = false)
    private String titulo;
    
    @NotBlank(message = "A content for yout post is a must!")
    @Column(name = "conteudo", length = 450, nullable = false)
    private String conteudo;
    
    @Column(name = "curtidas")
    private int curtidas;
    
    @Column(name = "criado_em")
    private LocalDateTime criado_em;
    
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario userId;
    
    public int getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getTitulo() {
        return this.titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getConteudo() {
        return this.conteudo;
    }
    
    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }
    
    public LocalDateTime getCriado_Em() {
        return this.criado_em;
    }
    
    public void setCriado_Em(LocalDateTime criado_em) {
        this.criado_em = criado_em;
    }
    
    public Usuario getUserId() {
        return this.userId;
    }
    
    public void setUserId(Usuario userId) {
        this.userId = userId;
    }
    
    public int getCurtidas() {
        return this.curtidas;
    }
    
    public void setCurtidas(int curtidas) {
        this.curtidas = curtidas;
    }
}
