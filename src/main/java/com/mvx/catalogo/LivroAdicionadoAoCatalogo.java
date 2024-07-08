package com.mvx.catalogo;

import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public record LivroAdicionadoAoCatalogo(String titulo, String numeroInventario,
        String isbn, String autor) {

}
