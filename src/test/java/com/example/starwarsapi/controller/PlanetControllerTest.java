package com.example.starwarsapi.controller;

import com.example.starwarsapi.domain.Planet;
import com.example.starwarsapi.dto.PlanetDTO;
import com.example.starwarsapi.dto.PlanetDTOPut;
import com.example.starwarsapi.service.PlanetService;
import com.example.starwarsapi.util.PlanetCreator;
import com.example.starwarsapi.util.PlanetDTOCreator;
import com.example.starwarsapi.util.PlanetDTOPutCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

@DisplayName("Tests Planets Controller")
@ExtendWith(SpringExtension.class)
class PlanetControllerTest {

    @InjectMocks
    private PlanetController planetController;
    @Mock
    private PlanetService planetServiceMock;

    @BeforeEach
    void setUp() {
        BDDMockito.when(planetServiceMock.listAll())
                .thenReturn(List.of(PlanetCreator.createValidPlanet()));

        BDDMockito.when(planetServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(PlanetCreator.createValidPlanet());

        BDDMockito.when(planetServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(PlanetCreator.createValidPlanet()));

        BDDMockito.when(planetServiceMock.save(ArgumentMatchers.any(PlanetDTO.class)))
                .thenReturn(PlanetCreator.createValidPlanet());

        BDDMockito.doNothing()
                .when(planetServiceMock)
                .replace(ArgumentMatchers.any(PlanetDTOPut.class));

        BDDMockito.doNothing()
                .when(planetServiceMock).delete(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("listAll returns list of planet when successful")
    void listAll_ReturnsListOfPlanets_WhenSuccessful() {
        String expectedName = PlanetCreator.createValidPlanet().getName();

        List<Planet> planets = planetController.listAll().getBody();

        Assertions.assertThat(planets).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(planets.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns planet when successful")
    void findById_ReturnsPlanet_WhenSuccessful() {
        Long expectedId = PlanetCreator.createValidPlanet().getId();
        Planet planet = planetController.findById(1).getBody();

        Assertions.assertThat(planet).isNotNull();
        Assertions.assertThat(planet.getId()).isNotNull().isEqualTo(1);
    }

    @Test
    @DisplayName("findByName returns a list of planet when successful")
    void findByName_ReturnsListOfPlanet_WhenSuccessful() {
        String expectedName = PlanetCreator.createValidPlanet().getName();
        List<Planet> planets = planetController.findByName("Any name").getBody();

        Assertions.assertThat(planets).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(planets.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns an empty list of planet when planet is not found")
    void findByName_ReturnsEmptyListOfPlanet_WhenPlanetIsNotFound() {
        BDDMockito.when(planetServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Planet> planets = planetController.findByName("planet").getBody();

        Assertions.assertThat(planets).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("save return planet when successful")
    void save_ReturnsPlanet_WhenSuccessful() {
        Planet planet = planetController.save(PlanetDTOCreator.createPlanetDTO()).getBody();

        Assertions.assertThat(planet).isNotNull().isEqualTo(PlanetCreator.createValidPlanet());
    }

    @Test
    @DisplayName("replace updates planet when succeesful")
    void replace_UpdatesPlanet_WhenSuccessful() {
        Assertions.assertThatCode(() ->
                        planetController.replace(PlanetDTOPutCreator.createPlanetDTOPut()))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = planetController.replace(PlanetDTOPutCreator.createPlanetDTOPut());

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    @DisplayName("delete removes planet when successful")
    void delete_RemovesPlanet_WhenSuccessful() {
        Assertions.assertThatCode(() -> planetController.delete(1)).doesNotThrowAnyException();

        ResponseEntity<Void> entity = planetController.delete(1);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}