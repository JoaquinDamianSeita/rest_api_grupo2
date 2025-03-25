package org.api.rest_api_grupo2.service;

import org.api.rest_api_grupo2.dto.request.RegisterRequest;
import org.api.rest_api_grupo2.dto.response.MessageResponseDto;

public interface IUserService {
    MessageResponseDto saveNewUser(RegisterRequest registerRequest);
}
