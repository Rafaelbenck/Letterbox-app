package com.example.myapplication;

public class Film {
    private String nome, nota, sinopse;

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public static class FilmsBuilder{
        private String nome, nota, sinopse;

        public FilmsBuilder setNome(String nome) {
            this.nome = nome;
            return this;
        }

        public FilmsBuilder setNota(String nota) {
            this.nota = nota;
            return this;
        }

        public FilmsBuilder setSinopse(String sinopse) {
            this.sinopse = sinopse;
            return this;
        }

        public  Film build(){
            Film filme = new Film();
            filme.nome = nome;
            filme.nota= nota;
            filme.sinopse= sinopse;
            return filme;
        }

        private FilmsBuilder(){}


        public static FilmsBuilder builder(){
            return new FilmsBuilder();
        }

    }
}
