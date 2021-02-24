package br.com.systemsgs.springbooot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetalhesPedidoDTO {

    private Integer codigo;
    private String cpf;
    private String nomeClinete;
    private BigDecimal total;
    private String dataPedido;
    private String status;
    private List<DetalhesItensPedidoDTO> items;

}
