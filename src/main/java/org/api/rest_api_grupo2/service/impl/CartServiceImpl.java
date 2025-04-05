package org.api.rest_api_grupo2.service.impl;

import org.api.rest_api_grupo2.repository.CartRepository;
import org.api.rest_api_grupo2.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    private CartRepository cartRepository;
}
