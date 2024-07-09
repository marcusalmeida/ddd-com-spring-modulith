package com.mvx.biblioteca.livro.domain;

import org.jmolecules.ddd.annotation.ValueObject;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "livros_emprestimo", uniqueConstraints = @UniqueConstraint(columnNames = { "barcode" }))
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Embedded
    private Barcode numeroInventario;

    private String isbn;

    @Enumerated(EnumType.STRING)
    private StatusLivro status;

    @Version
    private Long version;

    public boolean disponivel() {
        return StatusLivro.DISPONIVEL.equals(this.status);
    }

    public boolean reservado(){
        return StatusLivro.RESERVADO.equals(this.status);
    }

    public boolean indisponivel() {
        return StatusLivro.INDISPONIVEL.equals(this.status);
    }

    public Livro(String titulo, Barcode numeroInventario, String isbn) {
        this.titulo = titulo;
        this.numeroInventario = numeroInventario;
        this.isbn = isbn;
        this.status = StatusLivro.DISPONIVEL;
    }

    public Livro marqueIndisponivel() {
        if (indisponivel()) {
            throw new IllegalStateException("O livro já está indisponivel.");
        }
        this.status = StatusLivro.INDISPONIVEL;
        return this;
    }


    public Livro marqueReservado() {
        if (reservado()) {
            throw new IllegalStateException("O livro já está disponivel.");
        }
        this.status = StatusLivro.RESERVADO;
        return this;
    }


    public Livro marqueDisponivel() {
        if (disponivel()) {
            throw new IllegalStateException("O livro já está disponível.");
        }
        this.status = StatusLivro.DISPONIVEL;
        return this;
    }

    @ValueObject
    public record Barcode(String barcode) {
    }

    public enum StatusLivro {
        DISPONIVEL, INDISPONIVEL, RESERVADO
    }
}
