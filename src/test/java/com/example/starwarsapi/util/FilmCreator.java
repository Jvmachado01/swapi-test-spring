package com.example.starwarsapi.util;

import com.example.starwarsapi.domain.Film;

public class FilmCreator {

    public static Film createFilmToBeSaved() {
        return Film.builder()
                .title("A New Hope")
                .build();
    }

    public static Film createValidFilm() {
        return Film.builder()
                .id(1L)
                .title("A New Hope")
                .build();
    }

    public static Film createAnotherFilm() {
        return Film.builder()
                .title("Revenge of the Sith")
                .build();
    }
}
