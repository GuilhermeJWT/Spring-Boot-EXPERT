package br.com.systemsgs.springbooot.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotEmpty(message = "Informe a Descrição do Produto!")
    private String descricao;

    @NotNull(message = "Informe o Preço do Produto!")
    private BigDecimal preco_unitario;

}
