package org.api.rest_api_grupo2.service.impl;

import org.api.rest_api_grupo2.repository.SaleRepository;
import org.api.rest_api_grupo2.service.ISaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaleServiceImpl implements ISaleService {
    @Autowired
    private SaleRepository saleRepository;
}
