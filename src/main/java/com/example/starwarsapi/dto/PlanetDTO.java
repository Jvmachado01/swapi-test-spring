package com.example.starwarsapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlanetDTO {
    private String name;
    private String climate;
    private String terrain;
    private List<String> filmUrls;
}

