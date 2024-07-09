package com.mvx.biblioteca.emprestimo.application;

import java.time.LocalDate;

import com.mvx.biblioteca.emprestimo.domain.Emprestimo;

public record EmprestimoDto(
        Long id, String barcode, Long idAssociado, LocalDate dataDeReserva,
        LocalDate dataDeRetirada, int duracaoReservaEmDias, int duracaoEmprestimoEmDias,
        LocalDate dateDeEntrega, Emprestimo.StatusEmprestimo status) {
}