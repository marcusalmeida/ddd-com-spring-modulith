package com.mvx.emprestimo;

import java.time.LocalDate;

import com.mvx.emprestimo.Emprestimo.Livro;

public record EmprestimoDto(Long id,
        Livro livro,
        Long associadoId,
        LocalDate dataDeEmprestimo,
        int duracaoEmprestimoEmDias,
        LocalDate dataDeRetorno,
        Emprestimo.StatusEmprestimo status) {

}
