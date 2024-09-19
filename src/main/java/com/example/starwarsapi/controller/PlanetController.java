package com.example.starwarsapi.controller;

import com.example.starwarsapi.domain.Planet;
import com.example.starwarsapi.dto.PlanetDTO;
import com.example.starwarsapi.dto.PlanetDTOPut;
import com.example.starwarsapi.service.PlanetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("planets")
public class PlanetController {

    @Autowired
    private PlanetService planetService;

    @GetMapping
    public ResponseEntity<List<Planet>> listAll() {
        return ResponseEntity.ok(planetService.listAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Planet> findById(@PathVariable long id) {
        return ResponseEntity.ok(planetService.findByIdOrThrowBadRequestException(id));
    }

    @GetMapping(path = "/find")
    public ResponseEntity<List<Planet>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(planetService.findByName(name));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        planetService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<Planet> save(@RequestBody PlanetDTO planetDTO) {
        return new ResponseEntity<>(planetService.save(planetDTO),
                HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody PlanetDTOPut planetDTOPut) {
        planetService.replace(planetDTOPut);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/populate")
    public ResponseEntity<String> populatePlanets() {
        planetService.populateDatabaseWithPlanetsFromApi();
        return ResponseEntity.ok("Database populated with planets from API");
    }


}