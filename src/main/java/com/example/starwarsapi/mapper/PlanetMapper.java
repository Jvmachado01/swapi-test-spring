package com.example.starwarsapi.mapper;

import com.example.starwarsapi.domain.Planet;
import com.example.starwarsapi.dto.PlanetDTO;
import com.example.starwarsapi.dto.PlanetDTOPut;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PlanetMapper {

    Planet toEntityPost(PlanetDTO planetDTO);

    PlanetDTO toDTO(Planet planet);

    Planet toEntityPut(PlanetDTOPut planetDTOPut);

    PlanetDTOPut toDTOPut(Planet planet);
}