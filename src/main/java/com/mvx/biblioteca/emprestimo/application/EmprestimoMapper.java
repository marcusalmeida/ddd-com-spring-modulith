package com.mvx.biblioteca.emprestimo.application;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import com.mvx.biblioteca.emprestimo.domain.Emprestimo;
import com.mvx.biblioteca.livro.domain.Livro;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmprestimoMapper {

    @Mapping(source = "barcode", target = "barcode", qualifiedByName = "barcodeNormalizado")
    EmprestimoDto toDto(Emprestimo emprestimo);

    Emprestimo toEntity(EmprestimoDto dto);

    @Named("barcodeNormalizado")
    static String barcodeNormalizado(Livro.Barcode barcode) {
        return barcode.barcode();
    }
}
