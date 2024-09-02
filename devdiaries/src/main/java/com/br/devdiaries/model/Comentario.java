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
import java.time.LocalDateTime;

@Entity
@Table(name = "comentarios")
public class Comentario {

    public Comentario() {
    }
    
    public Comentario(Integer parentComment1) {
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @NotBlank(message = "Your comment needs to have some content!")
    @Column(name = "conteudo", columnDefinition = "TEXT", nullable = false)
    private String conteudo;
    
    @Column(name = "comentado_em")
    private LocalDateTime comentado_em;
    
    @Column(name = "curtidas")
    private int curtidas;
    
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario user;
    
    @ManyToOne
    @JoinColumn(name = "id_post")
    private Postagem post;
    
    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private Comentario parentComment;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public LocalDateTime getComentado_Em() {
        return comentado_em;
    }

    public void setComentado_Em(LocalDateTime comentado_em) {
        this.comentado_em = comentado_em;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public Postagem getPost() {
        return post;
    }

    public void setPost(Postagem post) {
        this.post = post;
    }

    public int getCurtidas() {
        return curtidas;
    }

    public void setCurtidas(int curtidas) {
        this.curtidas = curtidas;
    }

    public Comentario getParentComment() {
        return parentComment;
    }

    public void setParentComment(Comentario parentComment) {
        this.parentComment = parentComment;
    }
}
