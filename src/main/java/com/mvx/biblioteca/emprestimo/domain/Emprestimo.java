package com.mvx.biblioteca.emprestimo.domain;

import java.time.LocalDate;

import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;
import org.jmolecules.ddd.annotation.ValueObject;

import com.mvx.biblioteca.livro.domain.Livro.Barcode;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AggregateRoot
@Entity
@Table(name="emprestimos")
@Getter
@NoArgsConstructor
public class Emprestimo {

    @Identity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Barcode barcode;

    private Long idAssociado;

    private LocalDate dataDeRetirada;

    private LocalDate dataDeReserva;

    private int duracaoReservaEmDias;

    private int duracaoEmprestimoEmDias;

    private LocalDate dataDeEntrega;

    @Enumerated(EnumType.STRING)
    private StatusEmprestimo status;

    @Version
    private Long version;

    Emprestimo(String barcode, LocalDate dataDeReserva, Long idAssociado) {
        this.barcode = new Barcode(barcode);
        this.dataDeReserva = dataDeReserva;
        this.idAssociado = idAssociado;
        this.duracaoReservaEmDias = 3;
        this.status = StatusEmprestimo.RESERVADO;
    }

    public static Emprestimo of(String barcode, LocalDate dataDeReserva, Long isAssociado) {
        return new Emprestimo(barcode, dataDeReserva, isAssociado);
    }

    public boolean isAtivo() {
        return StatusEmprestimo.ATIVO.equals(this.status);
    }

    public boolean isReservado() {
        return StatusEmprestimo.RESERVADO.equals(this.status);
    }

    public boolean isVencido() {
        return StatusEmprestimo.VENCIDO.equals(this.status);
    }

    public boolean isConcluido() {
        return StatusEmprestimo.CONCLUIDO.equals(this.status);
    }

    public void concluir(LocalDate dataDeEntrega) {
        if (isConcluido()) {
            throw new IllegalStateException("Emprestimo não está ativo!");
        }
        this.status = StatusEmprestimo.CONCLUIDO;
        this.dataDeEntrega = dataDeEntrega;
    }

    public void ativar(LocalDate dataDeRetirada) {
        if (!isReservado()) {
            throw new IllegalStateException("Emprestimo não está reservadp!");
        }
        this.duracaoEmprestimoEmDias = 14;
        this.dataDeRetirada = dataDeRetirada;
        this.status = StatusEmprestimo.ATIVO;
    }

    @ValueObject
    public enum StatusEmprestimo {
        RESERVADO, ATIVO, VENCIDO, CONCLUIDO
    }
}
