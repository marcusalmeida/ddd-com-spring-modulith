package com.mvx.biblioteca.leitor.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "leitores")
public class Leitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Associacao status;

    Leitor(Associacao status) {
        this.status = status;
    }

    public static Leitor of(Associacao status) {
        return new Leitor(status);
    }

    public enum Associacao {
        ATIVO, INATIVO
    }
}
