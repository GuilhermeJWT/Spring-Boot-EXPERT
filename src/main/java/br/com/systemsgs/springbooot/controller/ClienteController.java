package br.com.systemsgs.springbooot.controller;

import br.com.systemsgs.springbooot.entity.Cliente;
import br.com.systemsgs.springbooot.repository.ClientesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/clientes")
public class ClienteController {

    @Autowired
    private ClientesRepository clientesRepository;

    @PostMapping("/salvar")
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente salvarCliente (@RequestBody @Valid Cliente cliente){

        return clientesRepository.save(cliente);

    }

    @GetMapping("/recupera/{id}")
    public Cliente getClienteById(@PathVariable Integer id){

        return clientesRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

    @GetMapping("/listartodos")
    public List<Cliente> listarTodosClientes(Cliente cliente){

        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase().withStringMatcher(
                ExampleMatcher.StringMatcher.CONTAINING);
        Example example = Example.of(cliente, matcher);

        return  clientesRepository.findAll(example);

    }

    @PutMapping("/editar/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void alterarCliente( @PathVariable Integer id, @RequestBody @Valid Cliente cliente){

        clientesRepository.findById(id).map(clienteExistente ->{
            cliente.setId(clienteExistente.getId());
            clientesRepository.save(cliente);
            return ResponseEntity.noContent().build();
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não Encontrado"));
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarCliente(@PathVariable Integer id){

        clientesRepository.findById(id).map(cliente -> {
                clientesRepository.delete(cliente);
                return cliente;
            }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não Encontrado"));

    }

}
