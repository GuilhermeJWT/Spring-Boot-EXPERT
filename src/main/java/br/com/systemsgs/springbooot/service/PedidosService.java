package br.com.systemsgs.springbooot.service;

import br.com.systemsgs.springbooot.dto.PedidoDTO;
import br.com.systemsgs.springbooot.entity.Pedido;
import br.com.systemsgs.springbooot.enums.StatusPedido;

import java.util.Optional;

public interface PedidosService {

    Pedido salvarPedidoService (PedidoDTO pedidoDTO);

    Optional<Pedido> obeterPedidoCompleto(Integer id);

    void updateStatusService(Integer id, StatusPedido statusPedido);

}
