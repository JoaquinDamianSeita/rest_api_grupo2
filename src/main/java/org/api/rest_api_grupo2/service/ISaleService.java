package org.api.rest_api_grupo2.service;

import java.util.List;
import org.apache.coyote.BadRequestException;
import org.api.rest_api_grupo2.dto.request.SaleCreateRequest;
import org.api.rest_api_grupo2.dto.response.SaleResponse;

public interface ISaleService {
    SaleResponse.NFTSaleResponse createSale(SaleCreateRequest request) throws BadRequestException;
    List<SaleResponse> getSalesByUser() throws BadRequestException;
}