package org.api.rest_api_grupo2.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.api.rest_api_grupo2.dto.request.RegisterRequest;
import org.api.rest_api_grupo2.dto.response.MessageResponseDto;
import org.api.rest_api_grupo2.exceptions.UnprocessableEntityException;
import org.api.rest_api_grupo2.model.User;
import org.api.rest_api_grupo2.repository.UserRepository;
import org.api.rest_api_grupo2.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public MessageResponseDto saveNewUser(RegisterRequest request) {
        Optional<User> existingUser = userRepository.findByEmail((request.getEmail()));

        if (existingUser.isPresent()) {
            throw new UnprocessableEntityException("El email ya está en uso.");
        }

        User user = objectMapper.convertValue(request, User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRegistration_date(LocalDateTime.now());

        userRepository.save(user);
        return new MessageResponseDto("Usuario registrado con éxito.");
    }
}
