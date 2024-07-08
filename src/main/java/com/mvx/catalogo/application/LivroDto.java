package com.mvx.catalogo.application;

import com.mvx.catalogo.domain.LivroCatalogo;

public record LivroDto(Long id, String titulo, LivroCatalogo.Barcode numeroCatalogo,
        String isbn, LivroCatalogo.Autor autor) {

}
