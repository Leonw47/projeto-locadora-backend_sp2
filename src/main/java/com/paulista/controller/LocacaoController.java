package com.paulista.controller;

import lombok.AllArgsConstructor;

import org.hibernate.annotations.common.util.impl.Log_$logger;
import org.springframework.format.annotation.DateTimeFormat;
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
import com.paulista.model.Locacao;
import com.paulista.repository.LocacaoRepository;

import java.sql.Date;


/*import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;*/

import java.util.List;

@RestController
@RequestMapping("/api/locacoes")
@AllArgsConstructor
//@Tag(name = "LocacaoController", description = "Fornece os serviços para acessar, alterar e salvar as informações")
public class LocacaoController {

    private static final String recurso = "Locacao";
    private final LocacaoRepository locacaoRepository;

    @GetMapping
    /*@Operation(description = "Lista as locações existentes.", responses = {
        @ApiResponse(responseCode = "200", description = "Caso as locações sejam encontradas."),
        @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
        @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")})
    */public List<Locacao> lista() {
        return locacaoRepository.findAll();
    }
    //Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Locacao> procurarPorID(@PathVariable Long id) {
        return locacaoRepository.findById(id)
            .map(recordFound -> ResponseEntity.ok().body(recordFound))
            .orElse(ResponseEntity.notFound().build());
    }

    //Criar
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    /*@Operation(description = "Salva a locação criada.", responses = {
        @ApiResponse(responseCode = "200", description = "Caso a locação seja salva."),
        @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
        @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")})
    */public Locacao criar(@RequestBody Locacao locacao) {
        return locacaoRepository.save(locacao);

    }

    //Editar
    @PutMapping("{id}")
    /*@Operation(description = "Edita a locacao criada.", responses = {
        @ApiResponse(responseCode = "200", description = "Caso a locacao seja editada."),
        @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
        @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")})
    */public ResponseEntity<Locacao> editar(@PathVariable Long id, @RequestBody Locacao locacao){
        return locacaoRepository.findById(id)
                .map(recordFound -> {
                    recordFound.setDataLocacao(locacao.getDataLocacao());
                    recordFound.setDataDevolucaoPrevista(locacao.getDataDevolucaoPrevista());
                    recordFound.setDataDevolucaoEfetiva(locacao.getDataDevolucaoEfetiva());
                    recordFound.setClientes(locacao.getClientes());
                    recordFound.setAlugados(locacao.getAlugados());
                    recordFound.setValorCobrado(locacao.getValorCobrado());
                    recordFound.setMultaCobrada(locacao.getMultaCobrada());
                    Locacao atualizado = locacaoRepository.save(recordFound);
                    return ResponseEntity.ok().body(atualizado);
                })
                .orElseThrow(() -> new RecursoNaoEncontradoException(recurso));
    }

    //Deletar
    @DeleteMapping("/{id}")
    /*@Operation(description = "Deleta a locação informada.", responses = {
        @ApiResponse(responseCode = "200", description = "Caso a locação seja deletada."),
        @ApiResponse(responseCode = "400", description = "O servidor não pode processar a requisição devido a alguma coisa que foi entendida como um erro do cliente."),
        @ApiResponse(responseCode = "500", description = "Caso não tenha sido possível realizar a operação.")})
    */public void deletar(@PathVariable Long id) {
        locacaoRepository.deleteById(id);

        // Após a exclusão, reordene os IDs
        List<Locacao> locacoes = locacaoRepository.findAll();
        Long novoId = Long.valueOf(1);
        for (Locacao locacao : locacoes) {
            locacao.setId(novoId++);
        }
        locacaoRepository.saveAll(locacoes);
    }

    @GetMapping("/clientes/{clienteId}")
    public ResponseEntity<List<Locacao>> locacoesPorCliente(
        @PathVariable Long clienteId,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataAtual) {
        List<Locacao> locacoes = locacaoRepository.findLocacoesAtivasPorCliente(clienteId, dataAtual);

        if (locacoes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(locacoes);
    }
}