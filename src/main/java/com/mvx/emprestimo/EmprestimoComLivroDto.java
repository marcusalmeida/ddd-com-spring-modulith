package com.mvx.emprestimo;

import java.time.LocalDate;

public record EmprestimoComLivroDto(Long emprestimoId,
        LocalDate dataDeCadastro, String livroBarcode, String tituloLivro, String autor) {

}
