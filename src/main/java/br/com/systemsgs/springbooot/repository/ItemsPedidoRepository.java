package br.com.systemsgs.springbooot.repository;

import br.com.systemsgs.springbooot.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemsPedidoRepository extends JpaRepository<ItemPedido, Integer> {

}
