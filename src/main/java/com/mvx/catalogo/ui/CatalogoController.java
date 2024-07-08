package com.mvx.catalogo.ui;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mvx.catalogo.application.GerenciadorCatalogo;
import com.mvx.catalogo.application.LivroDto;
import com.mvx.catalogo.domain.LivroCatalogo.Barcode;



@RestController
public class CatalogoController {

    private final GerenciadorCatalogo catalogo;

    public CatalogoController(GerenciadorCatalogo catalogo) {
        this.catalogo = catalogo;
    }

    @PostMapping("/catalogo/livros")
    public ResponseEntity<LivroDto> adicionarLivroAoCatalogo(@RequestBody AdicionarLivroRequest request) {
        LivroDto livro = catalogo.adicionarAoCatalogo(request.titulo(), new Barcode(request.numeroCatalogo()), request.isbn(), request.autor);
        return ResponseEntity.ok(livro);
    }

    @GetMapping("/catalogo/livros/{id}")
    public ResponseEntity<LivroDto> visualizarLivro(@PathVariable("id") Long id) {
        return catalogo.localizar(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    

    @GetMapping("/catalogo/livros")
    public ResponseEntity<List<LivroDto>> visualizarLivros() {
        return ResponseEntity.ok(catalogo.todosLivros());
    }
    
    record AdicionarLivroRequest(String titulo, String numeroCatalogo, String isbn, String autor) {
    }

}
