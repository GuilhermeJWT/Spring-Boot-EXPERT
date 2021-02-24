package br.com.systemsgs.springbooot.repository;

import br.com.systemsgs.springbooot.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutosRepository extends JpaRepository<Produto, Integer> {

}
