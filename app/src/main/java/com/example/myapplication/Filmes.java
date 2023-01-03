package com.example.myapplication;

import java.util.Arrays;
import java.util.List;

public class Filmes {
    public static List<Film> fakeFilms(){
        return Arrays.asList(

                Film.FilmsBuilder.builder()
                        .setNota("8")
                        .setSinopse("Nome mto show mesmo")
                        .setNome("As Longas Tranças de um calvo")
                        .build(),
                Film.FilmsBuilder.builder()
                        .setNota("9.5")
                        .setSinopse("Pela estrada fora")
                        .setNome("Onde os fracos não tem vez")
                        .build()

                );
    }
}
