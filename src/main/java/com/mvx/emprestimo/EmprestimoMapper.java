package com.mvx.emprestimo;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface EmprestimoMapper {
    EmprestimoDto toDto(Emprestimo emprestimo);

    Emprestimo toEntity(EmprestimoDto emprestimoDto);
}
