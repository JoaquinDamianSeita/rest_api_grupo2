package org.api.rest_api_grupo2.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.apache.coyote.BadRequestException;
import org.api.rest_api_grupo2.dto.request.SaleCreateRequest;
import org.api.rest_api_grupo2.dto.response.SaleResponse;
import org.api.rest_api_grupo2.service.ISaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sales")
public class SaleController {
    @Autowired
    private ISaleService saleService;

    @PostMapping
    public ResponseEntity<?> createSale(@Valid @RequestBody SaleCreateRequest request) throws BadRequestException {
        return new ResponseEntity<>(saleService.createSale(request), HttpStatus.OK);
    }

    @GetMapping // index
    public ResponseEntity<List<SaleResponse>> getSalesByRole() throws BadRequestException {
        List<SaleResponse> sales = saleService.getSalesByUser();
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }
}