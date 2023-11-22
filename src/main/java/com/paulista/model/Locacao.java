package com.paulista.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity //Entidade para mapeamento de banco de dados
public class Locacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("_id")
    private Long id;

    @Column
    private LocalDate dataLocacao;

    @Column
    private LocalDate dataDevolucaoPrevista;

    @Column
    private LocalDate dataDevolucaoEfetiva;

    @Column
    private Double valorCobrado;

    @Column
    private Double multaCobrada;

    @ManyToOne
    @JoinColumn(name = "idCliente")
    private Cliente clientes;

    @OneToMany
    @JoinTable(name="alugados", joinColumns= {@JoinColumn(name="idLocacao")}, inverseJoinColumns= {@JoinColumn(name="idItem")})
    private List<Item> alugados;

}
