package com.example.joao.facesenac.pi.activity.model;

public class FriendsList {

    private Long usuario1;
    private Long usuario2;
    private String nome;
    private boolean aprovado;

    public FriendsList(long usuario1, long usuario2, String nome, boolean aprovado) {
        this.usuario1 = usuario1;
        this.usuario2 = usuario2;
        this.nome = nome;
        this.aprovado = aprovado;
    }

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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isAprovado() {
        return aprovado;
    }

    public void setAprovado(boolean aprovado) {
        this.aprovado = aprovado;
    }
}
