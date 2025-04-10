package org.api.rest_api_grupo2.repository;

import java.util.List;

import org.api.rest_api_grupo2.model.Sale;
import org.api.rest_api_grupo2.model.SaleToken;
import org.api.rest_api_grupo2.model.serializables.SaleTokenId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleTokenRepository extends JpaRepository<SaleToken, SaleTokenId> {

    List<SaleToken> findBySale(Sale sale);
    
}
