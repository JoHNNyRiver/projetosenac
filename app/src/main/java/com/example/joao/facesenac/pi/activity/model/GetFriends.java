package com.example.joao.facesenac.pi.activity.model;

public class GetFriends {
    private Long usuario1;
    private Long usuario2;
    private Long id;
    private String nome;
    private String foto;
    private Boolean aprovado;

    private GetFriends() {}

    public Long getUsuario1() {
        return usuario1;
    }

    public void setUsuario1(Long usuario1) {
        this.usuario1 = usuario1;
    }

    public Long getUsuario2() {
        return usuario2;
    }

    public void setUsuario2(Long usuario2) {
        this.usuario2 = usuario2;
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
