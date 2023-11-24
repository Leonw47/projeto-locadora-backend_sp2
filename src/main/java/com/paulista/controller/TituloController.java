package com.paulista.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.paulista.exceptions.RecursoNaoEncontradoException;
import com.paulista.model.Titulo;
import com.paulista.repository.TituloRepository;

/*import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;*/

import java.util.List;

@RestController
@RequestMapping("/api/titulos")
@AllArgsConstructor
//@Tag(name = "TituloController", description = "Fornece os serviços para acessar, alterar e salvar as informações")
public class TituloController {

    private static final String recurso = "Título";
    private final TituloRepository tituloRepository;

    @GetMapping
    /*@Operation(description = "Lista os titulos existentes.", responses = {
        @ApiResponse(responseCode = "200", description = "Caso os titulos sejam encontrados."),
        @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
        @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")})
    */public List<Titulo> lista() {
        return tituloRepository.findAll();
    }

    //Procurar o ID
    @GetMapping("/{id}")
    /*@Operation(description = "Procura o titulo por id.", responses = {
        @ApiResponse(responseCode = "200", description = "Caso o titulo seja encontrado."),
        @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
        @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")})
    */public ResponseEntity<Titulo> procurarPorID(@PathVariable Long id) {
        return tituloRepository.findById(id)
                .map(recordFound -> ResponseEntity.ok().body(recordFound))
                .orElseThrow(() -> new RecursoNaoEncontradoException(recurso));
    }

    //Buscar Por Termo    
    @GetMapping("/search")
    public ResponseEntity<List<Titulo>> buscarPorTermo(@RequestParam String q) {
        try {
            // Aqui você pode chamar o método buscarPorTermo do repository
            List<Titulo> titulosEncontrados = tituloRepository.buscarPorTermo(q);
    
            if (titulosEncontrados.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
    
            return ResponseEntity.ok(titulosEncontrados);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Criar
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    /*@Operation(description = "Salva o titulo criado.", responses = {
        @ApiResponse(responseCode = "200", description = "Caso o titulo seja salvo."),
        @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
        @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")})
    */public Titulo criar(@RequestBody Titulo titulo) {
        return tituloRepository.save(titulo);

    }

    //Editar
    @PutMapping("/{id}")
    /*@Operation(description = "Edita o titulo criado.", responses = {
        @ApiResponse(responseCode = "200", description = "Caso o titulo sejam editado."),
        @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
        @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")})
    */public ResponseEntity<Titulo> editar(@PathVariable Long id, @RequestBody Titulo titulo){
        return tituloRepository.findById(id)
                .map(recordFound -> {
                    recordFound.setNome(titulo.getNome());
                    recordFound.setAno(titulo.getAno());
                    recordFound.setSinopse(titulo.getSinopse());
                    recordFound.setAtores(titulo.getAtores());
                    recordFound.setDiretor(titulo.getDiretor());
                    recordFound.setClasse(titulo.getClasse());
                    Titulo atualizado = tituloRepository.save(recordFound);
                    return ResponseEntity.ok().body(atualizado);
                })
                .orElseThrow(() -> new RecursoNaoEncontradoException(recurso));
    }

    //Deletar
    @DeleteMapping()
    /*@Operation(description = "Deleta o titulo informado.", responses = {
        @ApiResponse(responseCode = "200", description = "Caso o titulo sejam deletado."),
        @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
        @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")})
    */public void deletar(@RequestParam Long id){
        tituloRepository.deleteById(id);
    }
}
