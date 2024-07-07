INSERT INTO livros (id, titulo, barcode, isbn, autor, status)
VALUES
    (1, 'Sapiens', '13268510', '9780062316097', 'Yuval Noah Harari', 'DISPONIVEL'),
    (2, 'To Kill a Mockingbird', '49031878', '9780446310789', 'Harper Lee', 'INDISPONIVEL');

INSERT INTO emprestimos(id, livro_barcode, id_associado, data_de_emprestimo, duracao_emprestimo_em_dias, data_de_retorno, status)
VALUES (10, '49031878', NULL, '2023-03-11', 14, NULL, 'ATIVO');