package com.mvx.catalogo.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogoRepositorio extends JpaRepository<LivroCatalogo, Long> {
}
