package com.mvx.inventario;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class LivroController {
    
    private final GerenciadorLivros gerenciador;

    public LivroController(GerenciadorLivros gerenciador){
        this.gerenciador = gerenciador;
    }

    @PostMapping("/livros")
    public ResponseEntity<LivroDto> adicionarLivro(@RequestBody AdicionarLivroRequest request) {
        var livroDto = gerenciador.adicionarAoInventario(request.titulo(), new Livro.Barcode(request.numeroInventario()),request.isbn(), request.autor());
        
        return ResponseEntity.ok(livroDto);
    }
    
    @DeleteMapping("/livros/{id}")
    ResponseEntity<Void> removerLivroDoInventario(@PathVariable("id") Long id) {
        gerenciador.removerDoInventario(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/livros/{id}")
    public ResponseEntity<LivroDto> visualisarLivro(@PathVariable("id") Long id) {
        return gerenciador.localizar(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/livros")
    public ResponseEntity<List<LivroDto>> visualizarLivrosEmprestados() {
        return ResponseEntity.ok(gerenciador.livrosEmprestados());
    }
    
    

    record AdicionarLivroRequest(String titulo, String numeroInventario, String isbn, String autor){}
}
