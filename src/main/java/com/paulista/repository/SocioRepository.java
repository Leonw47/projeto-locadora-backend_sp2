package com.paulista.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paulista.model.Socio;

@Repository
public interface SocioRepository extends JpaRepository<Socio, Long> {
}
