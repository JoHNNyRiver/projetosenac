package com.example.joao.facesenac.pi.activity.model;

public class FeedPerfil {
    private Long id, usuario;
    private String texto, foto, data;

    private FeedPerfil(){}

    public FeedPerfil(Long id, Long usuario, String texto, String foto, String data) {
        this.id = id;
        this.usuario = usuario;
        this.texto = texto;
        this.foto = foto;
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(Long usuario) {
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
