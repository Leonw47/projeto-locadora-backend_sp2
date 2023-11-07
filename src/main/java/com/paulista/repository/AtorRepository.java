package com.paulista.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paulista.model.Ator;

@Repository
public interface AtorRepository extends JpaRepository<Ator, Long> {

}