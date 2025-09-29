package br.edu.ifpr.todo.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpr.todo.api.dto.TarefaRequest;
import br.edu.ifpr.todo.api.dto.TarefaResponse;
import br.edu.ifpr.todo.domain.model.Tarefa;
import br.edu.ifpr.todo.domain.model.TodoStatus;
import br.edu.ifpr.todo.domain.service.TarefaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {

    private final TarefaService service;

    public TarefaController(TarefaService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TarefaResponse criar(@Valid @RequestBody TarefaRequest dto) {
        Tarefa tarefa = service.criar(dto);
        return toResponse(tarefa);
    }

    @GetMapping
    public List<TarefaResponse> listar(
            @RequestParam(required = false) TodoStatus status,
            @RequestParam(required = false) Boolean importante) {

        List<Tarefa> tarefas = service.listar(null, status, importante, null);

        List<TarefaResponse> resposta = tarefas.stream()
                                               .map(this::toResponse)
                                               .toList();

        return resposta;
    }

    @GetMapping("/{id}")
    public TarefaResponse buscarPorId(@PathVariable Long id) {
        Tarefa tarefa = service.buscarPorId(id);
        return toResponse(tarefa);
    }

    @PatchMapping("/{id}")
    public TarefaResponse atualizarParcial(@PathVariable Long id,
                                           @Valid @RequestBody TarefaRequest dto) {

        Tarefa tarefa = service.atualizarParcial(id, dto);
        return toResponse(tarefa);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }

    private TarefaResponse toResponse(Tarefa tarefa) {
        return new TarefaResponse(
                tarefa.getId(),
                tarefa.getNome(),
                tarefa.getDescricao(),
                tarefa.getStatus(),
                tarefa.getDataCriacao(),
                tarefa.getDataEntrega(),
                tarefa.getImportante()
        );
    }
}
