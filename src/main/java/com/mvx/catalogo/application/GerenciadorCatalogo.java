package com.mvx.catalogo.application;

import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mvx.catalogo.LivroAdicionadoAoCatalogo;
import com.mvx.catalogo.domain.CatalogoRepositorio;
import com.mvx.catalogo.domain.LivroCatalogo;

@Service
@Transactional
public class GerenciadorCatalogo {

    private CatalogoRepositorio repositorio;
    private LivroMapper mapper;
    private ApplicationEventPublisher eventos;


    public GerenciadorCatalogo(CatalogoRepositorio repositorio, LivroMapper mapper, ApplicationEventPublisher eventos) {
        this.repositorio = repositorio;
        this.mapper = mapper;
        this.eventos = eventos;
    }

    /**
     * Adicionar um livro a biblioteca.
     */
    public LivroDto adicionarAoCatalogo(String titulo, LivroCatalogo.Barcode numeroCatalogo, String isbn,
            String nomeAutor) {
        LivroCatalogo livro = new LivroCatalogo(titulo, numeroCatalogo, isbn, new LivroCatalogo.Autor(nomeAutor));
        LivroDto dto = mapper.tDto(repositorio.save(livro));
        eventos.publishEvent(new LivroAdicionadoAoCatalogo(dto.titulo(), dto.numeroCatalogo().barcode(), dto.isbn(),
                dto.autor().nome()));
        return dto;
    }

    @Transactional(readOnly = true)
    public Optional<LivroDto> localizar(Long id) {
        return repositorio.findById(id).map(mapper::tDto);
    }

    @Transactional(readOnly = true)
    public List<LivroDto> todosLivros() {
        return repositorio.findAll()
                .stream()
                .map(mapper::tDto)
                .toList();
    }
}
