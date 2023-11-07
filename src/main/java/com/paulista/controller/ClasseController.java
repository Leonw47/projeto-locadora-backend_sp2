package com.paulista.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.paulista.exceptions.RecursoNaoEncontradoException;
import com.paulista.model.Classe;
import com.paulista.repository.ClasseRepository;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
@AllArgsConstructor
@Tag(name = "ClasseController", description = "Fornece os serviços para acessar, alterar e salvar as informações")
public class ClasseController {

    private static final String recurso = "Classe";
    private final ClasseRepository classeRepository;
    private static final Logger logger = LoggerFactory.getLogger(ClasseController.class);

    //Listar
    @GetMapping
    @Operation(description = "Lista as classes existentes por id.", responses = {
        @ApiResponse(responseCode = "200", description = "Caso as classes sejam encontradas."),
        @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
        @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")})
    public List<Classe> lista() {
        return classeRepository.findAll();
    }

    //Procurar o ID
    @GetMapping("/{id}")
    @Operation(description = "Procura as classes existentes por id.", responses = {
        @ApiResponse(responseCode = "200", description = "Caso as classes sejam encontradas."),
        @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
        @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")})
    public ResponseEntity<Classe> procurarPorID(@PathVariable Long id) {
        return classeRepository.findById(id)
                .map(recordFound -> ResponseEntity.ok().body(recordFound))
                .orElseThrow(() -> new RecursoNaoEncontradoException(recurso));
    }

    //Criar
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @Operation(description = "Salva as classes existentes por id.", responses = {
        @ApiResponse(responseCode = "200", description = "Caso as classes sejam salvas."),
        @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
        @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")})
    public Classe criar(@RequestBody Classe classe) {
        return classeRepository.save(classe);

    }

    //Editar
    @PutMapping("/{id}")
    /*@Operation(description = "Edita a classe informada.", responses = {
        @ApiResponse(responseCode = "200", description = "Caso a classe seja encontrada."),
        @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
        @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")})
    */public ResponseEntity<Classe> editar(@PathVariable Long id, @RequestBody Classe classe){
        return classeRepository.findById(id)
                .map(recordFound -> {
                    recordFound.setNome(classe.getNome());
                    recordFound.setData(classe.getData());
                    recordFound.setValor(classe.getValor());
                    Classe atualizado = classeRepository.save(recordFound);
                    return ResponseEntity.ok().body(atualizado);
                })
                .orElseThrow(() -> new RecursoNaoEncontradoException(recurso));
    }


    //Deletar
    @DeleteMapping()
    /*@Operation(description = "Deleta a classe informada.", responses = {
        @ApiResponse(responseCode = "200", description = "Caso a classe seja encontrada."),
        @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
        @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")})
    */public ResponseEntity<String> deletar(@RequestParam Long id){
        try {
            classeRepository.deleteById(id);
            return ResponseEntity.ok("Classe deletada com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao deletar a classe com ID: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar a classe. Verifique o log de erro para mais detalhes.");
        }
    }
}
