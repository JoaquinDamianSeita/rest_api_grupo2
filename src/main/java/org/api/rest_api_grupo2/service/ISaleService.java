package org.api.rest_api_grupo2.service;

import java.util.List;

import org.api.rest_api_grupo2.dto.request.SaleCreateRequest; // Ensure this class exists in the specified package
import org.api.rest_api_grupo2.dto.response.SaleResponse;
import org.api.rest_api_grupo2.model.Sale;


public interface ISaleService {

    Sale createSale(SaleCreateRequest request);
    List<Sale> getAllSales();
    SaleResponse toResponse(Sale sale);
    List<SaleResponse> getSalesByRole(Long userId);
}
