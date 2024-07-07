package com.mvx.inventario;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface LivroMapper {
    
    LivroDto toDto(Livro livro);
    Livro toEntity(LivroDto livroDto);
}
