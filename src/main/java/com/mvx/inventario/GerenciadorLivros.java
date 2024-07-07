package com.mvx.inventario;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class GerenciadorLivros {

    private final LivroRepositorio repositorio;
    private final LivroMapper mapper;

    public GerenciadorLivros(LivroRepositorio repositorio, LivroMapper mapper) {
        this.repositorio = repositorio;
        this.mapper = mapper;
    }

    /**
     * Adicionar um novo livro a biblioteca.
     */
    public LivroDto adicionarAoInventario(String titulo, Livro.Barcode numeroInventario, String isbn, String autor) {
        var novoLivro = new Livro(titulo, numeroInventario, isbn, new Livro.Autor(autor));
        return mapper.toDto(repositorio.save(novoLivro));
    }

    /**
     * Remover um livro da biblioteca.
     */
    public void removerDoInventario(Long idLivro) {
        Livro livro = repositorio.findById(idLivro)
                .orElseThrow(() -> new IllegalArgumentException("Livro nao encontrado"));

        if (livro.emprestado()) {
            throw new IllegalStateException("Livro emprestado atualmente.");
        }
        repositorio.delete(livro);
    }

    public void emprestar(String barcode) {
        var numeroInventario = new Livro.Barcode(barcode);

        Livro livro = repositorio.findByNumeroInventario(numeroInventario)
                .map(Livro::marqueEmprestado)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado."));

        repositorio.save(livro);
    }

    public void devolver(String barcode) {
        var numeroInventario = new Livro.Barcode(barcode);

        Livro livro = repositorio.findByNumeroInventario(numeroInventario)
                .map(Livro::marqueDisponivel)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado."));

        repositorio.save(livro);
    }

    @Transactional(readOnly = true)
    public Optional<LivroDto> localizar(Long id) {
        return repositorio.findById(id)
                .map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<LivroDto> livrosEmprestados() {
        return repositorio.findByStatus(Livro.StatusLivro.EMPRESTADO)
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}
