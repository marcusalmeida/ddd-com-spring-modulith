package com.mvx.biblioteca.livro.domain;

import java.time.LocalDate;

import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public record LivroRetornado(
    Long idLivro,
    String isbn,
    String numeroInventario,
    Long idLeitor,
    LocalDate dataDeRetorno
) {
    
}
