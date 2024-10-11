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


    public void populateDatabaseWithPlanetsFromApi() {
        int page = 1;
        boolean hasMorePages = true;

        while (hasMorePages) {
            String url = SWAPI_URL + "?page=" + page;
            log.debug("Fetching data from URL: {}", url);

            try {
                ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
                Map<String, Object> body = response.getBody();

                if (response.getStatusCode().is2xxSuccessful() && body != null) {
                    List<Map<String, Object>> results = (List<Map<String, Object>>) body.get("results");
                    String nextPageUrl = (String) body.get("next");

                    if (results == null || results.isEmpty()) {
                        hasMorePages = false;

                    } else {
                        List<PlanetDTO> planetsDTO = results.stream()
                                .map(result -> {
                                    PlanetDTO dto = new PlanetDTO();
                                    dto.setName((String) result.get("name"));
                                    dto.setClimate((String) result.get("climate"));
                                    dto.setTerrain((String) result.get("terrain"));
                                    dto.setFilmUrls((List<String>) result.get("films"));
                                    return dto;
                                })
                                .collect(Collectors.toList());

                        List<Planet> planets = planetsDTO.stream()
                                .map(dto -> {
                                    Planet planet = planetMapper.toEntityPost(dto);
                                    Set<Film> films = dto.getFilmUrls().stream()
                                            .map(this::fetchFilmDetails)
                                            .filter(Objects::nonNull)
                                            .collect(Collectors.toSet());
                                    planet.setFilms(films);
                                    return planet;
                                })
                                .collect(Collectors.toList());

                        List<Planet> existingPlanets = planetRepository.findByNameIn(planets.stream().map(Planet::getName).collect(Collectors.toList()));
                        Set<String> existingPlanetNames = existingPlanets.stream().map(Planet::getName).collect(Collectors.toSet());

                        List<Planet> planetsToSave = planets.stream()
                                .filter(planet -> !existingPlanetNames.contains(planet.getName()))
                                .collect(Collectors.toList());

                        planetRepository.saveAll(planetsToSave);
                        log.info("Saved {} new planets to the database.", planetsToSave.size());

                        hasMorePages = nextPageUrl != null;
                        page++;
                    }
                } else {
                    log.error("Failed to fetch data from SWAPI. Status code: {}", response.getStatusCode());
                    throw new RuntimeException("Failed to fetch data from SWAPI");
                }
            } catch (HttpClientErrorException e) {
                log.error("HTTP Error while fetching data from SWAPI: {}", e.getMessage());
                throw new RuntimeException("HTTP Error while fetching data from SWAPI", e);
            } catch (Exception e) {
                log.error("Unexpected error occurred: {}", e.getMessage());
                throw new RuntimeException("Unexpected error occurred while fetching data from SWAPI", e);
            }
        }
    }


    private Film fetchFilmDetails(String filmUrl) {
        try {
            log.debug("Fetching film details from URL: {}", filmUrl);
            ResponseEntity<Map> response = restTemplate.getForEntity(filmUrl, Map.class);
            Map<String, Object> body = response.getBody();

            if (response.getStatusCode().is2xxSuccessful() && body != null) {
                String title = (String) body.get("title");
                return filmRepository.findByTitle(title)
                        .orElseGet(() -> {
                            Film newFilm = new Film();
                            newFilm.setTitle(title);
                            filmRepository.save(newFilm);
                            return newFilm;
                        });
            } else {
                log.warn("Failed to fetch film details. Status code: {}", response.getStatusCode());
                return null;
            }
        } catch (HttpClientErrorException e) {
            log.error("HTTP Error while fetching film details: {}", e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("Unexpected error occurred while fetching film details: {}", e.getMessage());
            return null;
        }
    }

}
