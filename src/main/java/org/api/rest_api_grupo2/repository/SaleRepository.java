package org.api.rest_api_grupo2.repository;

import java.util.List;

import org.api.rest_api_grupo2.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findByUserId(Long userId);

}
