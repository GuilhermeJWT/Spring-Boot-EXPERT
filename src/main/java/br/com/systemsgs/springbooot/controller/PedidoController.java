package br.com.systemsgs.springbooot.controller;

import br.com.systemsgs.springbooot.dto.DetalhesItensPedidoDTO;
import br.com.systemsgs.springbooot.dto.DetalhesPedidoDTO;
import br.com.systemsgs.springbooot.dto.PedidoDTO;
import br.com.systemsgs.springbooot.dto.UpdateStatusPedidoDTO;
import br.com.systemsgs.springbooot.entity.ItemPedido;
import br.com.systemsgs.springbooot.entity.Pedido;
import br.com.systemsgs.springbooot.enums.StatusPedido;
import br.com.systemsgs.springbooot.service.PedidosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidosService pedidosService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integer salvarPedido(@RequestBody @Valid PedidoDTO pedidoDTO){
         Pedido pedido = pedidosService.salvarPedidoService(pedidoDTO);
         return pedido.getId();
    }

    @GetMapping("/buscarpedido/{id}")
    public DetalhesPedidoDTO getById(@PathVariable Integer id){
        return pedidosService.obeterPedidoCompleto(id)
         .map(p -> converter(p))
         .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não Encontrado!"));
    }

    @PatchMapping("/updatestatus/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizarStatus(@PathVariable Integer id, @RequestBody UpdateStatusPedidoDTO updateStatusPedidoDTO){

        String novoStatus = updateStatusPedidoDTO.getNovoStatus();

        pedidosService.updateStatusService(id, StatusPedido.valueOf(novoStatus));
    }

    private DetalhesPedidoDTO converter(Pedido pedido){
        return  DetalhesPedidoDTO.builder()
                .codigo(pedido.getId())
                .dataPedido(pedido.getData_Pedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .cpf(pedido.getCliente().getCpf())
                .nomeClinete(pedido.getCliente().getNome())
                .total(pedido.getTotal())
                .status(pedido.getStatus().name())
                .items(converter(pedido.getItens()))
                .build();

    }

    private List<DetalhesItensPedidoDTO> converter(List<ItemPedido> itens){
        if(CollectionUtils.isEmpty(itens)){
            return Collections.emptyList();
        }

        return itens.stream()
                .map( item -> DetalhesItensPedidoDTO.builder().descricaoProduto(item.getProduto().getDescricao())
                .precoUnitario(item.getProduto().getPreco_unitario())
                .quantidade(item.getQuantidade())
                 .build()
        ).collect(Collectors.toList());

    }

}
