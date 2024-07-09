package com.mvx.biblioteca.livro.domain;

import java.time.LocalDate;

public record LivroReservado(
        Long idLivro,
        String isbn,
        String numeroInventario,
        Long idLeitor,
        LocalDate dataDeReserva) {
}
