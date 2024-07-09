package com.mvx.biblioteca.emprestimo.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mvx.biblioteca.emprestimo.domain.Emprestimo.StatusEmprestimo;

public interface EmprestimoRepositorio extends JpaRepository<Emprestimo, Long> {

    List<Emprestimo> findByStatus(StatusEmprestimo status);
}
