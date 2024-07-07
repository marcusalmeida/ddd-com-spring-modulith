package com.mvx.emprestimo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

interface EmprestimoRepositorio extends JpaRepository<Emprestimo, Long>{

    @Query("""
            SELECT new com.mvx.emprestimo.EmprestimoComLivroDto(e.id, e.dataDeEmprestimo, e.livro.barcode, l.titulo, l.autor.nome)
            FROM Emprestimo e
            INNER JOIN Livro l ON e.livro.barcode = l.numeroInventario.barcode
            WHERE e.status = :status
            """)
    List<EmprestimoComLivroDto> findEmprestimoComStatus(Emprestimo.StatusEmprestimo status);

    Optional<Emprestimo> findById(@NonNull Long id);
}
