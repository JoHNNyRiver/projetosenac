package com.example.joao.facesenac.pi.activity.model;

public class PostUserLogin {
    private Long id;
    private String nome, email, senha;
    private Integer foto;
    private Integer temFoto;

    public Integer getTemfoto() {
        return temFoto;
    }

    public void setTemfoto(Integer temfoto) {
        this.temFoto = temfoto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Integer getFoto() {
        return foto;
    }

    public void setFoto(Integer foto) {
        this.foto = foto;
    }
}
