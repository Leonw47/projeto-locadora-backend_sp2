package com.paulista.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paulista.model.Titulo;

@Repository
public interface TituloRepository extends JpaRepository<Titulo, Long> {
}
