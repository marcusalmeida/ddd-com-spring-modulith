package com.mvx.biblioteca.livro.application;

import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mvx.biblioteca.livro.domain.Livro;
import com.mvx.biblioteca.livro.domain.LivroRepositorio;
import com.mvx.biblioteca.livro.domain.LivroReservado;
import com.mvx.biblioteca.livro.domain.LivroRetirado;
import com.mvx.biblioteca.livro.domain.LivroRetornado;
import com.mvx.biblioteca.livro.domain.Livro.Barcode;
import com.mvx.catalogo.LivroAdicionadoAoCatalogo;

import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class GerenciadorInventario {
    
    private static final String LIVRO_NAO_ENCONTRADO = "Livro não encontrado.";
    
    private final LivroRepositorio livros;

    @ApplicationModuleListener
    public void on(LivroAdicionadoAoCatalogo evento){
        livros.save(new Livro(evento.titulo(), new Barcode(evento.numeroInventario()), evento.isbn()));
    }

    @ApplicationModuleListener
    public void on(LivroReservado evento){
        Livro livro = livros.findById(evento.idLivro())
            .map(Livro::marqueReservado)
            .orElseThrow(() -> new IllegalArgumentException(LIVRO_NAO_ENCONTRADO));
        livros.save(livro);
    }

    @ApplicationModuleListener
    public void on(LivroRetirado evento) {
        Livro livro = livros.findById(evento.idLivro())
            .map(Livro::marqueIndisponivel)
            .orElseThrow(() -> new IllegalArgumentException(LIVRO_NAO_ENCONTRADO));
        livros.save(livro);
    }

    @ApplicationModuleListener
    public void on(LivroRetornado evento) {
        Livro livro = livros.findById(evento.idLivro())
            .map(Livro::marqueDisponivel)
            .orElseThrow(() -> new IllegalArgumentException(LIVRO_NAO_ENCONTRADO));
        livros.save(livro);
    }
    
}
