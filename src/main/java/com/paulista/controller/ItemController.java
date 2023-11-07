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

import com.paulista.exceptions.RecursoNaoEncontradoException;
import com.paulista.model.Item;
import com.paulista.repository.ItemRepository;

/*import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;*/

import java.util.List;

@RestController
@RequestMapping("/api/itens")
@AllArgsConstructor
//@Tag(name = "ItemController", description = "Fornece os serviços para acessar, alterar e salvar as informações")
public class ItemController {

    private static final String recurso = "Item";
    private final ItemRepository itemRepository;

    @GetMapping
    /*@Operation(description = "Lista os itens existentes.", responses = {
        @ApiResponse(responseCode = "200", description = "Caso os itens sejam encontrados."),
        @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
        @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")})
    */public List<Item> lista() {
        return itemRepository.findAll();
    }

    //Procurar o ID
    @GetMapping("/{id}")
    /*@Operation(description = "Procura o item por id.", responses = {
        @ApiResponse(responseCode = "200", description = "Caso o item seja encontrado."),
        @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
        @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")})
    */public ResponseEntity<Item> procurarPorID(@PathVariable Long id) {
        return itemRepository.findById(id)
                .map(recordFound -> ResponseEntity.ok().body(recordFound))
                .orElseThrow(() -> new RecursoNaoEncontradoException(recurso));
    }

    //Criar
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    /*@Operation(description = "Salva o item criado.", responses = {
        @ApiResponse(responseCode = "200", description = "Caso o item seja salvo."),
        @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
        @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")})
    */public Item criar(@RequestBody Item item) {
        return itemRepository.save(item);

    }

    //Editar
    @PutMapping("/{id}")
    /*@Operation(description = "Edita o item criado.", responses = {
        @ApiResponse(responseCode = "200", description = "Caso o item sejam editado."),
        @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
        @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")})
    */public ResponseEntity<Item> editar(@PathVariable Long id, @RequestBody Item item){
        return itemRepository.findById(id)
                .map(recordFound -> {
                    recordFound.setNumserie(item.getNumserie());
                    recordFound.setDtaquisicao(item.getDtaquisicao());
                    recordFound.setTipoItem(item.getTipoItem());
                    recordFound.setTitulo(item.getTitulo());
                    Item atualizado = itemRepository.save(recordFound);
                    return ResponseEntity.ok().body(atualizado);
                })
                .orElseThrow(() -> new RecursoNaoEncontradoException(recurso));
    }

    //Deletar
    @DeleteMapping()
    /*@Operation(description = "Deleta o item informado.", responses = {
        @ApiResponse(responseCode = "200", description = "Caso o item sejam deletado."),
        @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
        @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")})
    */public void deletar(@RequestParam Long id){
        itemRepository.deleteById(id);
    }
}
