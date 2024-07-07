package com.mvx.inventario;

public record LivroDto(Long id, String titulo, Livro.Barcode numeroInventario, String isbn, Livro.Autor autor,
        Livro.StatusLivro status) {

}
