package org.api.rest_api_grupo2.controller;

import org.api.rest_api_grupo2.service.INFTTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/nfts")
public class NFTTokenController {
    @Autowired
    private INFTTokenService nftTokenService;
}
