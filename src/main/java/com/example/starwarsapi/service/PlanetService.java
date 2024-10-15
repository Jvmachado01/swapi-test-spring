package com.example.starwarsapi.service;

import com.example.starwarsapi.domain.Film;
import com.example.starwarsapi.domain.Planet;
import com.example.starwarsapi.dto.PlanetDTO;
import com.example.starwarsapi.dto.PlanetDTOPut;
import com.example.starwarsapi.mapper.PlanetMapper;
import com.example.starwarsapi.repository.FilmRepository;
import com.example.starwarsapi.repository.PlanetRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class PlanetService {

    private static final Logger log = LoggerFactory.getLogger(PlanetService.class);
    private static final String SWAPI_URL = "https://swapi.dev/api/planets/";

    @Autowired
    private PlanetRepository planetRepository;
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private PlanetMapper planetMapper;


    public List<Planet> listAll() {
        return planetRepository.findAll();
    }

    public Planet findByIdOrThrowBadRequestException(long id) {
        try {
            return planetRepository.findById(id)
                    .orElseThrow(() -> new BadRequestException("Planet not found"));
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Planet> findByName(String name) {
        return planetRepository.findByName(name);
    }

    public void delete(long id) {
        planetRepository.delete(findByIdOrThrowBadRequestException(id));
    }

    public Planet save(PlanetDTO planetDTO) {
        log.info("Saving planet: {}", planetDTO);

        if (planetRepository.existsByName(planetDTO.getName())) {
            throw new IllegalArgumentException("Planet with name " +
                    planetDTO.getName() + " already exists.");
        }
   
        Planet planet = planetMapper.toEntityPost(planetDTO);
        log.info("Mapped planet: {}", planet);


        Set<Film> films = planetDTO.getFilmUrls().stream()
                .map(this::fetchFilmDetails)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        planet.setFilms(films);
        return planetRepository.save(planet);
    }

    public void replace(PlanetDTOPut planetDTOPut) {
        Planet planetSaved = findByIdOrThrowBadRequestException(planetDTOPut.getId());
        Planet planet = planetMapper.toEntityPut(planetDTOPut);
        planet.setId(planetSaved.getId());

        Set<Film> films = planetDTOPut.getFilmUrls().stream()
                .map(this::fetchFilmDetails)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        planet.setFilms(films);

        planetRepository.save(planet);
    }




}
