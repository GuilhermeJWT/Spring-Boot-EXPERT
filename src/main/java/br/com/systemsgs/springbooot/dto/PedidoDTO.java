package br.com.systemsgs.springbooot.dto;

import br.com.systemsgs.springbooot.validation.NotEmptyList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDTO {

    @NotNull(message = "Informe o Código do Cliente!")
    private Integer cliente;

    @NotNull(message = "Informe o Total do Pedido!")
    private BigDecimal total;

    @NotEmptyList(message = "Pedido não pode ser realizado sem Itens!")
    private List<ItemPedidoDTO> items;

}
