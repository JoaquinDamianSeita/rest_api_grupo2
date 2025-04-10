package org.api.rest_api_grupo2.service;

import org.apache.coyote.BadRequestException;
import org.api.rest_api_grupo2.dto.request.LoginRequest;
import org.api.rest_api_grupo2.dto.request.RegisterRequest;
import org.api.rest_api_grupo2.dto.request.UpdateRequest;
import org.api.rest_api_grupo2.dto.response.LoginResponseDto;
import org.api.rest_api_grupo2.dto.response.MessageResponseDto;
import org.api.rest_api_grupo2.model.User;

public interface IUserService {
    MessageResponseDto saveNewUser(RegisterRequest registerRequest);
    LoginResponseDto login(LoginRequest request);
    MessageResponseDto updateUser(Long id, UpdateRequest request);
    User validateAuthorizedUser(Long userId);
    User getAutheticatedUser() throws BadRequestException;
}
