package com.mvx.catalogo.application;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.mvx.catalogo.domain.LivroCatalogo;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LivroMapper {
    LivroDto tDto(LivroCatalogo livroCatalogo);
    LivroCatalogo toEntity(LivroDto livroDto);
}
