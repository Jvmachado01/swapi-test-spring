package com.example.starwarsapi.util;

import com.example.starwarsapi.domain.Film;
import com.example.starwarsapi.domain.Planet;

import java.util.HashSet;
import java.util.Set;

public class PlanetCreator {

    public static Planet createPlanetToBeSaved() {
        Film film = new Film();
        film.setTitle("A New Hope");
        Set<Film> films = new HashSet<>();
        films.add(film);

        return Planet.builder()
                .name("Tatooine")
                .climate("arid")
                .terrain("desert")
                .films(films)
                .build();
    }

    public static Planet createValidPlanet() {
        Film film = new Film();
        film.setTitle("A New Hope");
        Set<Film> films = new HashSet<>();
        films.add(film);

        return Planet.builder()
                .id(1L)
                .name("Tatooine")
                .climate("arid")
                .terrain("desert")
                .films(films)
                .build();
    }



    public static Planet createValidUpdatePlanet() {
        Film film = new Film();
        film.setTitle("Revenge of the Sith");
        Set<Film> films = new HashSet<>();
        films.add(film);

        return Planet.builder()
                .id(1L)
                .name("Alderaan")
                .climate("temperate")
                .terrain("glasslands, mounstains")
                .films(films)
                .build();
    }
}
