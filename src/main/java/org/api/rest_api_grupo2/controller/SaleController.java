package org.api.rest_api_grupo2.controller;

import org.api.rest_api_grupo2.service.ISaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sales")
public class SaleController {
    @Autowired
    private ISaleService saleService;
}
