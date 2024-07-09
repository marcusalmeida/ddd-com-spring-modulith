package com.mvx.biblioteca.emprestimo;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.mvx.biblioteca.emprestimo.application.GerenciadorEmprestimo;
import com.mvx.biblioteca.emprestimo.domain.Emprestimo.StatusEmprestimo;
import com.mvx.biblioteca.livro.domain.Livro;
import com.mvx.biblioteca.livro.domain.LivroRepositorio;
import com.mvx.biblioteca.livro.domain.LivroReservado;
import com.mvx.biblioteca.livro.domain.LivroRetirado;
import com.mvx.biblioteca.livro.domain.LivroRetornado;
import com.mvx.catalogo.LivroAdicionadoAoCatalogo;

@Transactional
@ApplicationModuleTest
class EmprestimoIntegrationTests {
    
    @DynamicPropertySource
    static void initializeData(DynamicPropertyRegistry registry){
        registry.add("spring.sql.init.data-locations", () -> "classpath:emprestimos.sql");
    }


    @Autowired
    GerenciadorEmprestimo emprestimos;

    @Autowired
    LivroRepositorio livros;

    @Test
    void deveriaCriarLivroOnLivroAdicionadoAoCatalogo(Scenario scenario) {
        scenario.publish(() -> new LivroAdicionadoAoCatalogo("A title", "9999", "73294", "An author"))
                .customize(it -> it.atMost(Duration.ofMillis(200)))
                .andWaitForStateChange(() -> livros.findByNumeroInventario(new Livro.Barcode("9999")))
                .andVerify(livro -> {
                    //noinspection OptionalGetWithoutIsPresent
                    assertThat(livro.get().getNumeroInventario().barcode()).isEqualTo("9999");
                    assertThat(livro.get().getIsbn()).isEqualTo("73294");
                });
    }

    @Test
    void deveriaCriarUmEmprestimoNoReservar(Scenario scenario) {
        scenario.stimulate(() -> emprestimos.reservar("13268510", 1L))
                .andWaitForEventOfType(LivroReservado.class)
                .toArriveAndVerify((event, dto) -> {
                    assertThat(event.numeroInventario()).isEqualTo("13268510");
                    assertThat(dto.status()).isEqualTo(StatusEmprestimo.RESERVADO);
                });
    }

    @Test
    void deveriaAtivarEmprestimoNoRetirar(Scenario scenario) {
        scenario.stimulate(() -> emprestimos.retirar(10L))
                .andWaitForEventOfType(LivroRetirado.class)
                .toArriveAndVerify((event, dto) -> {
                    assertThat(event.numeroInventario()).isEqualTo("49031878");
                    assertThat(dto.status()).isEqualTo(StatusEmprestimo.ATIVO);
                });
    }

    @Test
    void deveriaCompletarEmprestimoNoRetornar(Scenario scenario) {
        scenario.stimulate(() -> emprestimos.retornar(11L))
                .andWaitForEventOfType(LivroRetornado.class)
                .toArriveAndVerify((event, dto) -> {
                    assertThat(event.numeroInventario()).isEqualTo("37040952");
                    assertThat(dto.status()).isEqualTo(StatusEmprestimo.CONCLUIDO);
                });
    }

    @Test
    void deveriaListarEmprestimosAtivos() {
        var loans = this.emprestimos.emprestimosAtivos();
        assertThat(loans).isNotEmpty();
    }

    @Test
    void deveriaListarEmprestimosReservados() {
        var loans = this.emprestimos.emprestimosReservados();
        assertThat(loans).isNotEmpty();
    }
}
