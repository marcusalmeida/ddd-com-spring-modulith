package com.mvx.biblioteca.emprestimo.application;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mvx.biblioteca.emprestimo.domain.Emprestimo;
import com.mvx.biblioteca.emprestimo.domain.Emprestimo.StatusEmprestimo;
import com.mvx.biblioteca.emprestimo.domain.EmprestimoRepositorio;
import com.mvx.biblioteca.livro.domain.Livro;
import com.mvx.biblioteca.livro.domain.Livro.Barcode;
import com.mvx.biblioteca.livro.domain.LivroRepositorio;
import com.mvx.biblioteca.livro.domain.LivroReservado;
import com.mvx.biblioteca.livro.domain.LivroRetirado;
import com.mvx.biblioteca.livro.domain.LivroRetornado;

import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class GerenciadorEmprestimo {

    private static final String LIVRO_NAO_ENCONTRADO = "Livro não encontrado!";
    
    private final EmprestimoRepositorio emprestimos;
    private final LivroRepositorio livros;
    private final ApplicationEventPublisher eventos;
    private final EmprestimoMapper mapper;

    /**
     * Reservar um livro.
     * 
     * @param barcode indentificador unico do livro.
     */
    public EmprestimoDto reservar(String barcode, Long idAssociado) {
        Livro livro = livros.findByNumeroInventario(new Barcode(barcode))
                .orElseThrow(() -> new IllegalArgumentException(LIVRO_NAO_ENCONTRADO));

        if (!livro.disponivel()) {
            throw new IllegalStateException("Livro não disponível!");
        }

        LocalDate dataDeReserva = LocalDate.now();
        Emprestimo emprestimo = Emprestimo.of(barcode, dataDeReserva, idAssociado);
        EmprestimoDto dto = mapper.toDto(emprestimos.save(emprestimo));

        eventos.publishEvent(
                new LivroReservado(
                        livro.getId(),
                        livro.getIsbn(),
                        livro.getNumeroInventario().barcode(),
                        emprestimo.getIdAssociado(),
                        dataDeReserva));
        return dto;
    }

    /**
     * Retirar um livro previamente reservado.
     * 
     * @param idEmprestimo identificador unico do emprestimo do livro.
     */
    public EmprestimoDto retirar(Long idEmprestimo) {
        Emprestimo emprestimo = emprestimos.findById(idEmprestimo)
                .orElseThrow(() -> new IllegalArgumentException("Emprestimo não encontrado!"));

        Livro livro = livros.findByNumeroInventario(emprestimo.getBarcode())
                .orElseThrow(() -> new IllegalArgumentException(LIVRO_NAO_ENCONTRADO));

        if (!livro.reservado()) {
            throw new IllegalStateException("Livro não está reservado!");
        }

        LocalDate dataDeRetirada = LocalDate.now();
        emprestimo.ativar(dataDeRetirada);
        EmprestimoDto dto = mapper.toDto(emprestimos.save(emprestimo));

        eventos.publishEvent(
                new LivroRetirado(
                        livro.getId(),
                        livro.getIsbn(),
                        livro.getNumeroInventario().barcode(),
                        emprestimo.getIdAssociado(),
                        dataDeRetirada));
        return dto;

    }

    /**
     * Retornar um livro emprestado.
     * 
     * @param idEmprestimo identificador unico do emprestimo do livro
     */
    public EmprestimoDto retornar(Long idEmprestimo) {
        LocalDate dataDeEntrega = LocalDate.now();

        Emprestimo emprestimo = emprestimos.findById(idEmprestimo)
                .orElseThrow(() -> new IllegalArgumentException("Emprestimo não encontrado!"));
        emprestimo.concluir(dataDeEntrega);
        EmprestimoDto dto = mapper.toDto(emprestimos.save(emprestimo));


        Livro livro = livros.findByNumeroInventario(emprestimo.getBarcode())
                .orElseThrow(() -> new IllegalArgumentException(LIVRO_NAO_ENCONTRADO));

        eventos.publishEvent(
            new LivroRetornado(
                livro.getId(), 
                livro.getIsbn(), 
                livro.getNumeroInventario().barcode(), 
                emprestimo.getIdAssociado(), 
                dataDeEntrega)
        );

        return dto;
    }

    @Transactional(readOnly = true)
    public List<EmprestimoDto> emprestimosAtivos() {
        return emprestimos.findByStatus(StatusEmprestimo.ATIVO)
            .stream()
            .map(mapper::toDto)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<EmprestimoDto> emprestimosReservados() {
        return emprestimos.findByStatus(StatusEmprestimo.RESERVADO)
            .stream()
            .map(mapper::toDto)
            .toList();
    }

    @Transactional(readOnly = true)
    public Optional<EmprestimoDto> localizar(Long idEmprestimo) {
        return emprestimos.findById(idEmprestimo)
                .map(mapper::toDto);
    }
}
