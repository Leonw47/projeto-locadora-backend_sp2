package com.paulista.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paulista.model.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}
