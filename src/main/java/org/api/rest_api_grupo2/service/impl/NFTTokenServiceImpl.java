package org.api.rest_api_grupo2.service.impl;

import org.api.rest_api_grupo2.repository.NFTTokenRepository;
import org.api.rest_api_grupo2.service.INFTTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NFTTokenServiceImpl implements INFTTokenService {
    @Autowired
    private NFTTokenRepository nftTokenRepository;
}
