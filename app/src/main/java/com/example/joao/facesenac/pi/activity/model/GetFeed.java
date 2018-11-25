package com.example.joao.facesenac.pi.activity.model;

import java.util.Date;

public class GetFeed {
    private Long id;
    private Long usuario;
    private String texto;
    private String foto;
    private String data;
    private Integer numCurtidas;
    private String nomeUser;
    private String fotoUser;

    private GetFeed() {}

    public GetFeed(Long id, Long usuario, String texto, String foto, String data, Integer numCurtidas, String nomeUser, String fotoUser) {
        this.id = id;
        this.usuario = usuario;
        this.texto = texto;
        this.foto = foto;
        this.data = data;
        this.numCurtidas = numCurtidas;
        this.nomeUser = nomeUser;
        this.fotoUser = fotoUser;
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

    public Integer getNumCurtidas() {
        return numCurtidas;
    }

    public void setNumCurtidas(Integer numCurtidas) {
        this.numCurtidas = numCurtidas;
    }

    public String getNomeUser() {
        return nomeUser;
    }

    public void setNomeUser(String nomeUser) {
        this.nomeUser = nomeUser;
    }

    public String getFotoUser() {
        return fotoUser;
    }

    public void setFotoUser(String fotoUser) {
        this.fotoUser = fotoUser;
    }
}
