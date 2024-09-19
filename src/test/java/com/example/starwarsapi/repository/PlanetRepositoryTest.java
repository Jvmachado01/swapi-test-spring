package com.example.starwarsapi.repository;

import com.example.starwarsapi.domain.Planet;
import com.example.starwarsapi.util.PlanetCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("Tests for planet repository")
class PlanetRepositoryTest {
    @Autowired
    private PlanetRepository planetRepository;

    @Test
    @DisplayName("Save persists planet when successful")
    void save_PersistAnime_WhenSuccessful() {
        Planet planetToBeSaved = PlanetCreator.createPlanetToBeSaved();
        Planet planetSaved = this.planetRepository.save(planetToBeSaved);

        Assertions.assertThat(planetSaved).isNotNull();
        Assertions.assertThat(planetSaved.getId()).isNotNull();
        Assertions.assertThat(planetSaved.getName()).isEqualTo(planetToBeSaved.getName());
    }

    @Test
    @DisplayName("Save updates planet when sucessful")
    void save_UpdatesPlanet_WhenSuccessful() {
        Planet planetToBeSaved = PlanetCreator.createPlanetToBeSaved();
        Planet planetSaved = this.planetRepository.save((planetToBeSaved));

        planetSaved.setName("Name test");
        Planet planetUpdated = this.planetRepository.save(planetSaved);

        Assertions.assertThat(planetUpdated).isNotNull();
        Assertions.assertThat(planetUpdated.getId()).isNotNull();
        Assertions.assertThat(planetUpdated.getName()).isEqualTo(planetSaved.getName());
    }

    @Test
    @DisplayName("Delete removes planet when successful")
    void delete_RemovesPlanet_WhenSuccessful() {
        Planet planetToBeSaved = PlanetCreator.createPlanetToBeSaved();
        Planet planetSaved = this.planetRepository.save(planetToBeSaved);

        this.planetRepository.delete(planetSaved);
        Optional<Planet> planetOptional = this.planetRepository.findById(planetSaved.getId());

        Assertions.assertThat(planetOptional).isEmpty();
    }

    @Test
    @DisplayName("Find by name returns list of planet when successful")
    void findByName_ReturnsListOfPlanet_WhenSuccessful() {
        Planet planetToBeSaved = PlanetCreator.createPlanetToBeSaved();
        Planet planetSaved = this.planetRepository.save(planetToBeSaved);
        String name = planetSaved.getName();

        List<Planet> planets = this.planetRepository.findByName(name);

        Assertions.assertThat(planets).isNotEmpty().contains(planetSaved);

    }

    @Test
    @DisplayName("Find by name returns empty list when planet is not found")
    void findByName_ReturnsEmptyList_WhenPlanetIsNotFound() {
        List<Planet> planets = this.planetRepository.findByName("nenhum");

        Assertions.assertThat(planets).isEmpty();
    }

}