package com.mvx.emprestimo;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mvx.emprestimo.Emprestimo.StatusEmprestimo;
import com.mvx.inventario.GerenciadorLivros;

@Transactional
@Service
public class GerenciadorEmprestimos {
    
    private final EmprestimoRepositorio repositorio;
    private final GerenciadorLivros livros;
    private final EmprestimoMapper mapper;

    public GerenciadorEmprestimos(EmprestimoRepositorio repositorio, GerenciadorLivros livros, EmprestimoMapper mapper) {
        this.repositorio = repositorio;
        this.livros = livros;
        this.mapper = mapper;
    }

    /**
     * Empresta um livre.
     * 
     * @param codigo de barras único indentificador do livro.
     */
     public EmprestimoDto checkout(String barcode) {
        livros.indisponibilizar(barcode);
        Emprestimo emprestimo = Emprestimo.of(barcode);
        Emprestimo emprestimoSalvo = repositorio.save(emprestimo);
        return mapper.toDto(emprestimoSalvo);
     }

     /**
      * Retornar um emprestimo de livro.
      *
      * @param idEmprestimo identificador unico do livro emprestado.
      */
      public EmprestimoDto checkin(Long idEmprestimo){
        Emprestimo emprestimo = repositorio.findById(idEmprestimo)
            .orElseThrow(() -> new IllegalArgumentException("Emprestimo não encontrado."));
        
        livros.disponibilizar(emprestimo.getLivro().barcode());
        emprestimo.concluir();
        return mapper.toDto(repositorio.save(emprestimo));
      }


      @Transactional(readOnly = true)
      public List<EmprestimoComLivroDto> emprestimosAtivos() {
        return repositorio.findEmprestimoComStatus(StatusEmprestimo.ATIVO);
      }

      @Transactional(readOnly = true)
      public Optional<EmprestimoDto> localizar(Long idEmprestimo){
        return repositorio.findById(idEmprestimo)
            .map(mapper::toDto);
      }
}

    
