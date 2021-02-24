package br.com.systemsgs.springbooot.controller;

import br.com.systemsgs.springbooot.entity.Produto;
import br.com.systemsgs.springbooot.repository.ProdutosRepository;
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
@RequestMapping(value = "/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutosRepository produtosRepository;

    @PostMapping("/salvar")
    public Produto salvarProduto(@RequestBody @Valid Produto produto){

        return produtosRepository.save(produto);

    }

    @GetMapping("/recupera/{id}")
    public Produto getProdutoId(@PathVariable Integer id){

        return produtosRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

    @GetMapping("/listartodos")
    public List<Produto> listarTodosProdutos(Produto produto){

        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase().withStringMatcher(
                ExampleMatcher.StringMatcher.CONTAINING);
        Example example = Example.of(produto, matcher);

        return produtosRepository.findAll(example);

    }

    @PutMapping("/editar/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void alterarProduto(@PathVariable Integer id, @RequestBody @Valid Produto produto){

        produtosRepository.findById(id).map(produtoExistente -> {
            produto.setId(produtoExistente.getId());
            produtosRepository.save(produto);
            return ResponseEntity.noContent().build();
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não Encontrado"));

    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarProduto (@PathVariable Integer id){

        produtosRepository.findById(id).map(produto -> {
            produtosRepository.delete(produto);
            return produto;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não Encontrado"));

    }

}
