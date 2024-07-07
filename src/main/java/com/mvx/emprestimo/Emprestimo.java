package com.mvx.emprestimo;

import java.time.LocalDate;

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
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="emprestimos")
@Getter
@NoArgsConstructor
public class Emprestimo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name="barcode", column = @Column(name="livro_barcode"))
    private Livro livro;

    @Column(name="id_associado")
    private Long idAssociado;

    @Column(name="data_de_emprestimo")
    private LocalDate dataDeEmprestimo;

    @Column(name="duracao_emprestimo_em_dias")
    private int duracaoEmprestimoEmDias;

    @Column(name="data_de_retorno")
    private LocalDate dataDeRetorno;

    @Enumerated(EnumType.STRING)
    private StatusEmprestimo status;

    @Version
    private Long version;

    Emprestimo(String barcodeLivro){
        this.livro = new Livro(barcodeLivro);
        this.dataDeEmprestimo = LocalDate.now();
        this.duracaoEmprestimoEmDias = 14;
        this.status = StatusEmprestimo.ATIVO;
    }

    public static Emprestimo of(String barcodeLivro){
        return new Emprestimo(barcodeLivro);
    }

    public boolean isAtivo(){
        return StatusEmprestimo.ATIVO.equals(this.status);
    }

    public boolean isAtrasado(){
        return StatusEmprestimo.ATRASADO.equals(this.status);
    }

    public boolean isConcluido(){
        return StatusEmprestimo.CONCLUIDO.equals(this.status);
    }


    public void concluir() {
        if (isConcluido()){
            throw new IllegalStateException("Emprestimo não está ativo.");
        }
        this.status = StatusEmprestimo.CONCLUIDO;
        this.dataDeRetorno = LocalDate.now();
    }

    public enum StatusEmprestimo {
        ATIVO, ATRASADO, CONCLUIDO
    }

    public record Livro(String barcode){}
}
