package com.example.starwarsapi.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Planet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String climate;
    private String terrain;

    @ManyToMany
    @JoinTable(
            name = "planet_film",
            joinColumns = @JoinColumn(name = "planet_id"),
            inverseJoinColumns = @JoinColumn(name = "film_id")
    )
    private Set<Film> films = new HashSet<>();
}

