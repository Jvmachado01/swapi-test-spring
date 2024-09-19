package com.example.starwarsapi.repository;

import com.example.starwarsapi.domain.Film;
import com.example.starwarsapi.util.FilmCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@DataJpaTest
@DisplayName("Tests for film repository")
class FilmRepositoryTest {
    @Autowired
    private FilmRepository filmRepository;

    @Test
    @DisplayName("Save film when successful")
    @Transactional
    void saveFilme_PersistsFilm_WhenSuccessful() {
        Film filmToBeSaved = FilmCreator.createFilmToBeSaved();
        Film filmSaved = this.filmRepository.save(filmToBeSaved);

        Assertions.assertThat(filmSaved).isNotNull();
        Assertions.assertThat(filmSaved.getId()).isNotNull();
        Assertions.assertThat(filmSaved.getTitle()).isEqualTo(filmToBeSaved.getTitle());
    }

    @Test
    @DisplayName("Save updates films when successful")
    @Transactional
    void save_UpdatesFilm_WhenSuccessful() {
        Film filmToBeSaved = FilmCreator.createFilmToBeSaved();
        Film filmSaved = this.filmRepository.save(filmToBeSaved);

        filmSaved.setTitle("new title");
        Film filmUpdated = this.filmRepository.save(filmSaved);

        Assertions.assertThat(filmUpdated).isNotNull();
        Assertions.assertThat(filmUpdated.getId()).isNotNull();
        Assertions.assertThat(filmUpdated.getTitle()).isEqualTo(filmSaved.getTitle());

    }

    @Test
    @DisplayName("Find by name returns list of films when successful")
    @Transactional
    void findByTitleFilm_ReturnsListOfFilm_WhenSuccess() {
        Film filmToBeSaved = FilmCreator.createFilmToBeSaved();
        Film filmSaved = this.filmRepository.save(filmToBeSaved);

        String title = filmSaved.getTitle();
        Optional<Film> films = this.filmRepository.findByTitle(title);

        Assertions.assertThat(films).isNotNull().contains(filmSaved);
    }

    @Test
    @DisplayName("Delete removes film when successful")
    @Transactional
    void delete_RemovesPlanet_WhenSuccessful() {
        Film filmToBeSaved = FilmCreator.createFilmToBeSaved();
        Film filmSaved = this.filmRepository.save(filmToBeSaved);

        this.filmRepository.delete(filmSaved);
        Optional<Film> filmOptional = this.filmRepository.findById(filmSaved.getId());

        Assertions.assertThat(filmOptional).isEmpty();
    }


}