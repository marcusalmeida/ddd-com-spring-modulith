package com.mvx.catalogo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.mvx.catalogo.application.GerenciadorCatalogo;
import com.mvx.catalogo.domain.CatalogoRepositorio;
import com.mvx.catalogo.domain.LivroCatalogo.Barcode;

@Transactional
@ApplicationModuleTest
class CatalogoIntegrationTests {

    @DynamicPropertySource
    static void initializeData(DynamicPropertyRegistry registry) {
        registry.add("spring.sql.init.data-locations", () -> "classpath:catalogo_livros.sql");
    }

    @Autowired
    GerenciadorCatalogo catalogo;

    @Autowired
    CatalogoRepositorio repositorio;

    @Test
    void shouldAddBookToCatalog(Scenario scenario) {
        scenario.stimulate(() -> catalogo.adicionarAoCatalogo("Titulo", new Barcode("999"), "654", "An author"))
                .andCleanup(livroDto -> repositorio.deleteById(livroDto.id()))
                .andWaitForEventOfType(LivroAdicionadoAoCatalogo.class)
                .toArriveAndVerify((event, dto) -> {
                    assertThat(event.titulo()).isEqualTo("Titulo");
                    assertThat(event.numeroInventario()).isEqualTo("999");
                    assertThat(event.isbn()).isEqualTo("654");
                    assertThat(event.autor()).isEqualTo("An author");
                    assertThat(dto.id()).isNotNull();
                });
    }

    @Test
    void deveriaListarTodosLivros() {
        var livros = catalogo.todosLivros();
        assertThat(livros).hasSizeBetween(3, 4);
    }
}
