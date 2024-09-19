package com.example.starwarsapi.service;

import com.example.starwarsapi.domain.Planet;
import com.example.starwarsapi.dto.PlanetDTO;
import com.example.starwarsapi.dto.PlanetDTOPut;
import com.example.starwarsapi.mapper.PlanetMapper;
import com.example.starwarsapi.repository.PlanetRepository;
import com.example.starwarsapi.util.PlanetCreator;
import com.example.starwarsapi.util.PlanetDTOCreator;
import com.example.starwarsapi.util.PlanetDTOPutCreator;
import org.apache.coyote.BadRequestException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@DisplayName("Tests Service")
@ExtendWith(SpringExtension.class)
class PlanetServiceTest {

    @InjectMocks
    private PlanetService planetService;
    @Mock
    PlanetMapper planetMapper;
    @Mock
    private PlanetRepository planetRepositoryMock;


    @BeforeEach
    void SetUp() {
        BDDMockito.when(planetRepositoryMock.findAll())
                .thenReturn(List.of(PlanetCreator.createValidPlanet()));

        BDDMockito.when(planetRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(PlanetCreator.createValidPlanet()));

        BDDMockito.when(planetRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(PlanetCreator.createValidPlanet()));

        BDDMockito.when(planetRepositoryMock.save(ArgumentMatchers.any(Planet.class)))
                .thenReturn(PlanetCreator.createValidPlanet());

        BDDMockito.when(planetMapper.toEntityPost(ArgumentMatchers.any(PlanetDTO.class)))
                .thenReturn(PlanetCreator.createValidPlanet());

        BDDMockito.when(planetMapper.toEntityPut(ArgumentMatchers.any(PlanetDTOPut.class)))
                .thenReturn(PlanetCreator.createValidPlanet());

        BDDMockito.doNothing().when(planetRepositoryMock).delete(ArgumentMatchers.any(Planet.class));

    }

    @Test
    @DisplayName("listAll returns list of planet when successful")
    void listAll_ReturnsListOfPlanets_WhenSuccessful() {
        String expectedName = PlanetCreator.createValidPlanet().getName();
        List<Planet> planets = planetService.listAll();

        Assertions.assertThat(planets).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(planets.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException returns planet when successful")
    void findByIdOrThrowBadRequestException_WhenSuccessful() {
        Long expectedId = PlanetCreator.createValidPlanet().getId();
        Planet planet = planetService.findByIdOrThrowBadRequestException(1);

        Assertions.assertThat(planet).isNotNull();
        Assertions.assertThat(planet.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException throws BadRequestException when planet is nod found")
    void findByIdOrThrowBadRequestException_ThrowsBadRequestException_WhenPlanetIsNotFound() {
        BDDMockito.when(planetRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> planetService.findByIdOrThrowBadRequestException(1))
                .withCauseInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("findByName returns a list of planet when successful")
    void findByName_ReturnsListOfPlanet_WhenSuccessful() {
        String expectedName = PlanetCreator.createValidPlanet().getName();

        List<Planet> planets = planetService.findByName("planet");

        Assertions.assertThat(planets).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(planets.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns an empty list of planet when planet is not found")
    void findByName_ReturnsEmptyList_WhenFilmIsNotFound() {
        BDDMockito.when(planetRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Planet> planets = planetService.findByName("planet");

        Assertions.assertThat(planets).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("save returns planet when success")
    void save_ReturnsPlanet_WhenSuccessful() {
        Planet planet = planetService.save(PlanetDTOCreator.createPlanetDTO());

        Assertions.assertThat(planet).isNotNull().isEqualTo(PlanetCreator.createValidPlanet());
    }

    @Test
    @DisplayName("replace updates planet when successful")
    void replace_UpdatesPlanet_WhenSuccess() {
        Assertions.assertThatCode(() ->
                        planetService.replace(PlanetDTOPutCreator.createPlanetDTOPut()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete removes planet when successful")
    void delete_RemovesPlanet_WhenSuccessful() {
        Assertions.assertThatCode(() -> planetService.delete(1))
                .doesNotThrowAnyException();
    }

}