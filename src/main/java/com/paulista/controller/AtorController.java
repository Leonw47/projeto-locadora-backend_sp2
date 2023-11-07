package com.paulista.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.paulista.exceptions.RecursoNaoEncontradoException;
import com.paulista.model.Ator;
import com.paulista.repository.AtorRepository;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/atores")
@AllArgsConstructor
@Tag(name = "AtorController", description = "Fornece os serviços para acessar, alterar e salvar as informações")
public class AtorController{

    private static final String recurso = "Ator";
    private final AtorRepository atorRepository;
    private static final Logger logger = LoggerFactory.getLogger(AtorController.class);

    //Listar
    @GetMapping
    @Operation(description = "Lista os atores existentes por id.", responses = {
        @ApiResponse(responseCode = "200", description = "Caso os atores sejam encontrados."),
        @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
        @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")})
    public List<Ator> lista() {
        return atorRepository.findAll();
    }

    //Procurar o ID
    @GetMapping("/{id}")
    @Operation(description = "Procura os atores existentes por id.", responses = {
        @ApiResponse(responseCode = "200", description = "Caso os atores sejam encontrados."),
        @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
        @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")})
    public ResponseEntity<Ator> procurarPorID(@PathVariable Long id) {
        return atorRepository.findById(id)
                .map(recordFound -> ResponseEntity.ok().body(recordFound))
                .orElseThrow(() -> new RecursoNaoEncontradoException(recurso));
    }

    //Criar
    @PostMapping
    @Operation(description = "Dado o nome, cadastra um novo ator.", responses = {
        @ApiResponse(responseCode = "200", description = "Caso o ator seja incluído com sucesso."),
        @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
        @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")})
    @ResponseStatus(code = HttpStatus.CREATED)
    public Ator criar(@RequestBody Ator ator) {
        return atorRepository.save(ator);

    }

    //Editar
    @PutMapping("/{id}")
    @Operation(description = "Dado o nome, edita o ator existente.", responses = {
        @ApiResponse(responseCode = "200", description = "Caso o ator seja editado com sucesso."),
        @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
        @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")})
    public ResponseEntity<Ator> editar(@PathVariable Long id, @RequestBody Ator ator){
        return atorRepository.findById(id)
                .map(recordFound -> {
                    recordFound.setNome(ator.getNome());
                    Ator atualizado = atorRepository.save(recordFound);
                    return ResponseEntity.ok().body(atualizado);
                })
                .orElseThrow(() -> new RecursoNaoEncontradoException(recurso));
    }



    // Deletar
    @DeleteMapping()
//    @Operation(description = "Dado o nome, deleta o ator existente.", responses = {
//        @ApiResponse(responseCode = "200", description = "Caso o ator seja deletado com sucesso."),
//        @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
//        @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")})
    public ResponseEntity<String> deletar(@RequestParam Long id) {
        try {
            atorRepository.deleteById(id);
            return ResponseEntity.ok("Ator deletado com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao deletar o ator com ID: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar o ator. Verifique o log de erro para mais detalhes.");
        }
    }
}