package com.mvx.inventario;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class GerenciadorLivrosTest {

    @Autowired
    GerenciadorLivros gerenciadorLivros;

    @Test
    void deveriaListarLivrosEmprestados() {
        var livros = gerenciadorLivros.livrosEmprestados();
        assertThat(livros).hasSize(1);
        assertThat(livros.get(0).status()).isEqualTo(Livro.StatusLivro.EMPRESTADO);
    }
}
