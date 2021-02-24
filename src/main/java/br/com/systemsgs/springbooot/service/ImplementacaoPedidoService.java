package br.com.systemsgs.springbooot.service;

import br.com.systemsgs.springbooot.dto.ItemPedidoDTO;
import br.com.systemsgs.springbooot.dto.PedidoDTO;
import br.com.systemsgs.springbooot.entity.Cliente;
import br.com.systemsgs.springbooot.entity.ItemPedido;
import br.com.systemsgs.springbooot.entity.Pedido;
import br.com.systemsgs.springbooot.entity.Produto;
import br.com.systemsgs.springbooot.enums.StatusPedido;
import br.com.systemsgs.springbooot.exception.GenerationExceptionClass;
import br.com.systemsgs.springbooot.exception.PedidoNaoEncontradoException;
import br.com.systemsgs.springbooot.repository.ClientesRepository;
import br.com.systemsgs.springbooot.repository.ItemsPedidoRepository;
import br.com.systemsgs.springbooot.repository.PedidosRepository;
import br.com.systemsgs.springbooot.repository.ProdutosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImplementacaoPedidoService implements PedidosService{

    @Autowired
    private PedidosService pedidoServiceRepository;

    @Autowired
    private ClientesRepository clientesRepository;

    @Autowired
    private ProdutosRepository produtosRepository;

    @Autowired
    private ItemsPedidoRepository itemsPedidoRepository;

    @Autowired
    private PedidosRepository pedidosRepository;

    @Override
    @Transactional
    public Pedido salvarPedidoService(PedidoDTO pedidoDTO) {

        Integer idCliente = pedidoDTO.getCliente();
        Cliente clientePedido =  clientesRepository.findById(idCliente).orElseThrow(() -> new GenerationExceptionClass("Código do Cliente Inválido!"));

        Pedido pedido = new Pedido();
        pedido.setTotal(pedidoDTO.getTotal());
        pedido.setData_Pedido(LocalDate.now());
        pedido.setCliente(clientePedido);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> listaItensPedido =  converterItens(pedido, pedidoDTO.getItems());
        pedidosRepository.save(pedido);
        itemsPedidoRepository.saveAll(listaItensPedido);
        pedido.setItens(listaItensPedido);

        return pedido;
    }

    @Override
    public Optional<Pedido> obeterPedidoCompleto(Integer id) {
        return pedidosRepository.findByIdFetchItens(id);
    }

    @Override
    @Transactional
    public void updateStatusService(Integer id, StatusPedido statusPedido) {
        pedidosRepository.findById(id).map(pedido -> {
            pedido.setStatus(statusPedido);
            return pedidosRepository.save(pedido);
        }).orElseThrow(() -> new PedidoNaoEncontradoException());
    }

    private List<ItemPedido> converterItens(Pedido pedido, List<ItemPedidoDTO> itemsPedidosDTO){

        if(itemsPedidosDTO.isEmpty()){
            throw new GenerationExceptionClass("Informe os Itens para poder realizar o Pedido!");
        }

        return itemsPedidosDTO.stream().map( dto -> {
            Integer idProduto = dto.getProduto();
            Produto produto =  produtosRepository.findById(idProduto).orElseThrow(() -> new GenerationExceptionClass("Código do Produto Inválido"));

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setQuantidade(dto.getQuantidade());
            itemPedido.setPedido(pedido);
            itemPedido.setProduto(produto);
            return itemPedido;
        }).collect(Collectors.toList());

    }
}
