package com.mvx.emprestimo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.mvx.emprestimo.Emprestimo.StatusEmprestimo;
import com.mvx.inventario.GerenciadorLivros;

@Transactional
@SpringBootTest
class GerenciadorEmprestimosTests {

    @Autowired
    GerenciadorEmprestimos emprestimos;

    @Autowired
    GerenciadorLivros livros;

    @Test
    void deveriaCriarEmprestimoAndIndisponibilizarLivroNoCheckout() {
        var loanDto = emprestimos.checkout("13268510");
        assertThat(loanDto.status()).isEqualTo(StatusEmprestimo.ATIVO);
        assertThat(loanDto.livro().barcode()).isEqualTo("13268510");
        assertThat(livros.localizar(1L).get().status()).hasToString("INDISPONIVEL");
    }

    @Test
    void deveriaConcluirEmprestimoEDisponibilizarLivroNoCheckin() {
        var loan = emprestimos.checkin(10L);
        assertThat(loan.status()).isEqualTo(StatusEmprestimo.CONCLUIDO);
        assertThat(livros.localizar(2L).get().status()).hasToString("DISPONIVEL");
    }

    @Test
    void deveriaListarEmprestimosAtivos() {
        var emprestimosAtivos = this.emprestimos.emprestimosAtivos();
        assertThat(emprestimosAtivos).hasSize(1);
    }
}
