package com.mvx.catalogo.domain;

import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AggregateRoot
@Entity
@Getter
@NoArgsConstructor
@Table(name = "catalogo_livros", uniqueConstraints = @UniqueConstraint(columnNames = { "barcode" }))
public class LivroCatalogo {

    @Identity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Embedded
    private Barcode numeroCatalogo;

    private String isbn;

    @Embedded
    @AttributeOverride(name = "nome", column = @Column(name = "autor"))
    private Autor autor;

    @Version
    private Long version;

    public LivroCatalogo(String titulo, Barcode numeroCatalogo, String isbn, Autor autor) {
        this.titulo = titulo;
        this.numeroCatalogo = numeroCatalogo;
        this.isbn = isbn;
        this.autor = autor;
    }

    public record Barcode(String barcode) {
    }

    public record Autor(String nome) {
    }
}
