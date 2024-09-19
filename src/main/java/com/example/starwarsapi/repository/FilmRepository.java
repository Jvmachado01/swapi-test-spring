package com.example.starwarsapi.repository;

import com.example.starwarsapi.domain.Film;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FilmRepository extends JpaRepository<Film, Long> {
    Optional<Film> findByTitle(String title);
}