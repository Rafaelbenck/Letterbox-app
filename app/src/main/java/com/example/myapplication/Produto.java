package com.example.myapplication;

public class Produto {
    public String id;
    public String nome,genero, sinopse , idFilm;

    public Produto() {
    }

    public Produto(String nome, String genero) {
        this.nome = nome;
        this.genero = genero;
    }


    public Produto(String id, String nome, String genero) {
        this.id = id;
        this.nome = nome;
        this.genero = genero;
    }

    public Produto(String id,String nome, String genero, String idUsuario) {
        this.id = id;
        this.nome = nome;
        this.genero = genero;
        this.idFilm = idUsuario;
    }

    //@Override
//    public String toString(){
//        return nome + " | " + genero;
//    }

    public String getIdUsuario() {
        return idFilm;
    }

    public void setIdUsuario(String idUsuario) {
        this.idFilm = idUsuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }
}
