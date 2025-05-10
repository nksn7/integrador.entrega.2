package br.com.unifacisa.projetointegrador.repository;

import br.com.unifacisa.projetointegrador.domain.Venda;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Venda entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {}
