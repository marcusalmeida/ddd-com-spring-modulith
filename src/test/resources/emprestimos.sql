INSERT INTO livros_emprestimo (id, version, titulo, barcode, isbn, status)
VALUES (3999, 0, 'Sapiens', '13268510', '9780062316097', 'DISPONIVEL'),
       (3998, 0, 'To Kill a Mockingbird', '49031878', '9780446310789', 'RESERVADO'),
       (3997, 0, '1984', '37040952', '9780451520500', 'INDISPONIVEL');

INSERT INTO emprestimos (id, version, barcode, id_associado, data_de_reserva, data_de_retirada,
                  duracao_reserva_em_dias,
                  duracao_emprestimo_em_dias, data_de_entrega, status)
VALUES (10, 0, '49031878', 800, '2023-03-11', NULL, 3, 14, NULL, 'RESERVADO'),
       (11, 0, '37040952', 800, '2023-03-24', '2023-03-25', 3, 14, NULL, 'ATIVO');

INSERT INTO leitores (id, status)
VALUES (800, 'ATIVO');