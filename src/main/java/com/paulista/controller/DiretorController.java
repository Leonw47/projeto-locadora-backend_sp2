package com.paulista.controller;

import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.paulista.exceptions.RecursoNaoEncontradoException;
import com.paulista.model.Diretor;
import com.paulista.repository.DiretorRepository;

/*import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;*/

import java.util.List;

@RestController
@RequestMapping("/api/diretores")
@AllArgsConstructor
//@Tag(name = "DiretorController", description = "Fornece os serviços para acessar, alterar e salvar as informações")
public class DiretorController{

    private static final String recurso = "Diretor";
    private final DiretorRepository diretorRepository;
    private static final Logger logger = LoggerFactory.getLogger(DiretorController.class);

    //Listar
    @GetMapping
    /*@Operation(description = "Lista os diretores existentes por id.", responses = {
        @ApiResponse(responseCode = "200", description = "Caso os diretores sejam encontrados."),
        @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
        @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")})
    */public List<Diretor> lista() {
        return diretorRepository.findAll();
    }

    //Procurar o ID
    @GetMapping("/{id}")
    /*@Operation(description = "Procura os diretores existentes por id.", responses = {
        @ApiResponse(responseCode = "200", description = "Caso os diretores sejam encontrados."),
        @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
        @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")})
    */public ResponseEntity<Diretor> procurarPorID(@PathVariable Long id) {
        return diretorRepository.findById(id)
                .map(recordFound -> ResponseEntity.ok().body(recordFound))
                .orElseThrow(() -> new RecursoNaoEncontradoException(recurso));
    }

    //Criar
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    /*@Operation(description = "Salva o diretor criado.", responses = {
        @ApiResponse(responseCode = "200", description = "Caso o diretor sejam salvo."),
        @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
        @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")})
    */public Diretor criar(@RequestBody Diretor diretor) {
        return diretorRepository.save(diretor);

    }

    //Editar
    @PutMapping("/{id}")
    /*@Operation(description = "Edita o diretor informado.", responses = {
        @ApiResponse(responseCode = "200", description = "Caso o diretor seja editado."),
        @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
        @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")})
    */public ResponseEntity<Diretor> editar(@PathVariable Long id, @RequestBody Diretor diretor){
        return diretorRepository.findById(id)
                .map(recordFound -> {
                    recordFound.setNome(diretor.getNome());
                    Diretor atualizado = diretorRepository.save(recordFound);
                    return ResponseEntity.ok().body(atualizado);
                })
                .orElseThrow(() -> new RecursoNaoEncontradoException(recurso));
    }


    //Deletar
    @DeleteMapping()
    /*@Operation(description = "Deleta o diretor informado.", responses = {
        @ApiResponse(responseCode = "200", description = "Caso o diretor seja deletado."),
        @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
        @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")})
    */public ResponseEntity<String> deletar(@RequestParam Long id){
        try {
            diretorRepository.deleteById(id);
            return ResponseEntity.ok("Diretor deletado com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao deletar o diretor com ID: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar o diretor. Verifique o log de erro para mais detalhes.");
        }
    }
}