package com.mvx.biblioteca.leitor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mvx.biblioteca.leitor.domain.Leitor;
import com.mvx.biblioteca.leitor.domain.LeitorRepositorio;
import com.mvx.biblioteca.leitor.domain.Leitor.Associacao;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class LeitoresInitializer {
    
    private final LeitorRepositorio repositorio;

    @Bean
    CommandLineRunner init(){ 
        return args -> repositorio.save(Leitor.of(Associacao.ATIVO));
    }
}
