package com.example.starwarsapi.util;

import com.example.starwarsapi.domain.Film;
import com.example.starwarsapi.domain.Planet;
import com.example.starwarsapi.dto.PlanetDTOPut;

import java.util.ArrayList;


public class PlanetDTOPutCreator {
    public static PlanetDTOPut createPlanetDTOPut() {
        Planet validUpdatePlanet = PlanetCreator.createValidUpdatePlanet();
        System.out.println("Planet ID: " + validUpdatePlanet.getId());

        return PlanetDTOPut.builder()
                .id(validUpdatePlanet.getId())
                .name(validUpdatePlanet.getName())
                .climate(validUpdatePlanet.getClimate())
                .terrain(validUpdatePlanet.getTerrain())
                .filmUrls(new ArrayList<>(validUpdatePlanet.getFilms().stream()
                        .map(Film::getTitle)
                        .toList()))
                .build();

    }
}