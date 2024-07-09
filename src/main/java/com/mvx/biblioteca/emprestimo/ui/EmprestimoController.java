package com.mvx.biblioteca.emprestimo.ui;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mvx.biblioteca.emprestimo.application.EmprestimoDto;
import com.mvx.biblioteca.emprestimo.application.GerenciadorEmprestimo;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EmprestimoController {

    private final GerenciadorEmprestimo emprestimos;

    @PostMapping("/biblioteca/emprestimos")
    public ResponseEntity<EmprestimoDto> reservarLivro(@RequestBody ReservarRequest request) {
        EmprestimoDto dto = emprestimos.reservar(request.barcode(), request.idAssociado());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/biblioteca/emprestimos/{id}/retirar")
    public ResponseEntity<EmprestimoDto> retirarLivro(@PathVariable("id") Long idEmprestimo) {
        EmprestimoDto dto = emprestimos.retirar(idEmprestimo);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/biblioteca/emprestimos/{id}/retornar")
    public ResponseEntity<EmprestimoDto> retornarLivro(@PathVariable("id") Long idEmprestimo) {
        EmprestimoDto dto = emprestimos.retornar(idEmprestimo);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/biblioteca/emprestimos/{id}")
    public ResponseEntity<EmprestimoDto> visualizarUmEmprestimo(@PathVariable("id") Long idEmprestimo) {
        return emprestimos.localizar(idEmprestimo)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/biblioteca/emprestimos")
    public ResponseEntity<List<EmprestimoDto>> visualizarEmprestimosAtivos(@RequestParam String type) {
        if ("reservado".equalsIgnoreCase(type)) {
            return ResponseEntity.ok(emprestimos.emprestimosReservados());
        }
        return ResponseEntity.ok(emprestimos.emprestimosAtivos());
    }

    public record ReservarRequest(String barcode, Long idAssociado) {
    }

}
