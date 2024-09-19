package com.example.starwarsapi.util;

import com.example.starwarsapi.domain.Film;
import com.example.starwarsapi.domain.Planet;
import com.example.starwarsapi.dto.PlanetDTO;

import java.util.ArrayList;

public class PlanetDTOCreator {
    public static PlanetDTO createPlanetDTO() {
        Planet planetToBeSaved = PlanetCreator.createPlanetToBeSaved();
        return PlanetDTO.builder()
                .name(planetToBeSaved.getName())
                .climate(planetToBeSaved.getClimate())
                .terrain(planetToBeSaved.getTerrain())
                .filmUrls(new ArrayList<>(planetToBeSaved.getFilms().stream()
                        .map(Film::getTitle)
                        .toList()))
                .build();
    }
}
