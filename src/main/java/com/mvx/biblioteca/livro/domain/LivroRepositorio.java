package com.mvx.biblioteca.livro.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mvx.biblioteca.livro.domain.Livro.Barcode;

public interface LivroRepositorio extends JpaRepository<Livro, Long> {
    
    Optional<Livro> findByNumeroInventario(Barcode numeroInventario);
}
