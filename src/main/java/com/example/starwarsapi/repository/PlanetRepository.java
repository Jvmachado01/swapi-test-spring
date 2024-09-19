package com.example.starwarsapi.repository;

import com.example.starwarsapi.domain.Planet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanetRepository extends JpaRepository<Planet, Long> {
   List<Planet> findByNameIn(List<String> names);

   List<Planet> findByName(String name);

   boolean existsByName(String name);
}
