package com.example.joao.facesenac.pi.activity.model;

public class PostFeed {
    private Long usuario;
    private String texto;
    private String foto;

    public Long getId() {
        return usuario;
    }

    public void setId(Long usuario) {
        this.usuario = usuario;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
