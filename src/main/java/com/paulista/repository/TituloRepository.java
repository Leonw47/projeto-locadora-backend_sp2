package com.paulista.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.paulista.model.Titulo;

@Repository
public interface TituloRepository extends JpaRepository<Titulo, Long> {

    @Query("SELECT t FROM Titulo t " +
           "WHERE LOWER(t.nome) LIKE LOWER(CONCAT('%', :q, '%')) " +
           "OR LOWER(t.sinopse) LIKE LOWER(CONCAT('%', :q, '%')) " +
           "OR LOWER(t.categoria) LIKE LOWER(CONCAT('%', :q, '%')) " +
           "OR LOWER(t.diretor.nome) LIKE LOWER(CONCAT('%', :q, '%')) " +
           "OR EXISTS (SELECT a FROM t.atores a WHERE LOWER(a.nome) LIKE LOWER(CONCAT('%', :q, '%')))")
    List<Titulo> buscarPorTermo(@Param("q") String q);

}
