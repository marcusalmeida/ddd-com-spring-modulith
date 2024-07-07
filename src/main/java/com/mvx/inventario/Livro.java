package com.mvx.inventario;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
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
@Table(name = "livros", uniqueConstraints = @UniqueConstraint(columnNames = { "barcode" }))
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Embedded
    private Barcode numeroInventario;

    private String isbn;

    @Embedded
    @AttributeOverride(name = "nome", column = @Column(name = "autor"))
    private Autor autor;

    @Enumerated(EnumType.STRING)
    private StatusLivro status;

    @Version
    private Long version;

    public boolean disponivel() {
        return StatusLivro.DISPONIVEL.equals(this.status);
    }

    public boolean indisponivel() {
        return StatusLivro.INDISPONIVEL.equals(this.status);
    }

    public Livro(String titulo, Barcode numeroInventario, String isbn, Autor autor) {
        this.titulo = titulo;
        this.numeroInventario = numeroInventario;
        this.isbn = isbn;
        this.autor = autor;
        this.status = StatusLivro.DISPONIVEL;
    }

    public Livro marqueIndisponivel() {
        if (indisponivel()) {
            throw new IllegalStateException("O livro já está indisponivel.");
        }
        this.status = StatusLivro.INDISPONIVEL;
        return this;
    }

    public Livro marqueDisponivel() {
        if (disponivel()) {
            throw new IllegalStateException("O livro já está disponível.");
        }
        this.status = StatusLivro.DISPONIVEL;
        return this;
    }

    public record Barcode(String barcode) {
    }

    public record Autor(String nome) {
    }

    public enum StatusLivro {
        DISPONIVEL, INDISPONIVEL
    }
}
