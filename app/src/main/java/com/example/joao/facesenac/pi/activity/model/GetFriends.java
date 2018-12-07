package com.example.joao.facesenac.pi.activity.model;

public class GetFriends {
    private Long id;
    private String nome;
    private String foto;
    private Boolean aprovado;
    private String amizade;
    private String email;
    private String senha;
    private Integer temfoto;

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

    public Integer getTemfoto() {
        return temfoto;
    }

    public void setTemfoto(Integer temfoto) {
        this.temfoto = temfoto;
    }

    private GetFriends() {}

    public String getAmizade() {
        return amizade;
    }

    public void setAmizade(String statusAmizade) {
        this.amizade = statusAmizade;
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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Boolean getAprovado() {
        return aprovado;
    }

    public void setAprovado(Boolean aprovado) {
        this.aprovado = aprovado;
    }
}
