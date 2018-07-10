package com.example.pedrolemos.livrosfinal.model;

public class UserFavorites {

    public String nome, autor, descricao, categoria, capa;

    public UserFavorites(String nome, String autor, String descricao, String categoria, String capa) {
        this.nome = nome;
        this.autor = autor;
        this.descricao = descricao;
        this.categoria = categoria;
        this.capa = capa;
    }

    public UserFavorites() {
    }
}
