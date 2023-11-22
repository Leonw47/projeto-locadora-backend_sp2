/*package com.paulista.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.paulista.model.Locacao;

@Repository
public interface LocacaoRepository extends JpaRepository<Locacao, Long> {

     @Query("SELECT l FROM Locacao l " +
            "WHERE :dataAtual IS NULL OR l.dataDevolucaoEfetiva IS NULL AND l.dataDevolucaoPrevista >= :dataAtual " +
            "AND :clienteId IN (SELECT c.id FROM l.clientes c)")
    List<Locacao> findLocacoesAtivasPorCliente(
            @Param("clienteId") Long clienteId,
            @Param("dataAtual") Date dataAtual);

            
}*/
package com.paulista.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.paulista.model.Locacao;

@Repository
public interface LocacaoRepository extends JpaRepository<Locacao, Long> {

    @Query("SELECT l FROM Locacao l " +
            "WHERE (:dataAtual IS NULL OR l.dataDevolucaoEfetiva IS NULL AND l.dataDevolucaoPrevista >= :dataAtual) " +
            "AND l.clientes.id = :clienteId")
    List<Locacao> findLocacoesAtivasPorCliente(
            @Param("clienteId") Long clienteId,
            @Param("dataAtual") Date dataAtual);
}

