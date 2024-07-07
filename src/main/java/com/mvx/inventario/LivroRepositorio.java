package com.mvx.inventario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


interface LivroRepositorio extends JpaRepository<Livro, Long> {
    
    Optional<Livro> findByIsbn(String isbn);

    Optional<Livro> findByNumeroInventario(Livro.Barcode numeroInventario);

    List<Livro> findByStatus(Livro.StatusLivro status);
}
