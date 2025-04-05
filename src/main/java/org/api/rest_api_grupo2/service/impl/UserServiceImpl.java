package org.api.rest_api_grupo2.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.api.rest_api_grupo2.dto.request.LoginRequest;
import org.api.rest_api_grupo2.dto.request.RegisterRequest;
import org.api.rest_api_grupo2.dto.response.LoginResponseDto;
import org.api.rest_api_grupo2.dto.response.MessageResponseDto;
import org.api.rest_api_grupo2.exceptions.NotAuthorizedException;
import org.api.rest_api_grupo2.exceptions.UnprocessableEntityException;
import org.api.rest_api_grupo2.jwt.JwtUtil;
import org.api.rest_api_grupo2.model.Role;
import org.api.rest_api_grupo2.model.User;
import org.api.rest_api_grupo2.repository.UserRepository;
import org.api.rest_api_grupo2.repository.RoleRepository;
import org.api.rest_api_grupo2.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public MessageResponseDto saveNewUser(RegisterRequest request) {
        Optional<User> existingUser = userRepository.findByEmail((request.getEmail()));

        if (existingUser.isPresent()) {
            throw new UnprocessableEntityException("El email ya está en uso.");
        }

        Optional<Role> role = roleRepository.findById(request.getRoleId());

        if (role.isEmpty()) {
            throw new UnprocessableEntityException("El rol no existe.");
        }

        User user = objectMapper.convertValue(request, User.class);
        user.setRole(role.get());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRegistrationDate(LocalDateTime.now());

        userRepository.save(user);
        return new MessageResponseDto("Usuario registrado con éxito.");
    }

    @Override
    public LoginResponseDto login(LoginRequest request) {
        Optional<User> user = userRepository.findByUsername(request.getUsername());
        if (user.isEmpty()) {
            throw new NotAuthorizedException("Credenciales incorrectas.");
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtil.generateToken(userDetails);
            String expirationDate = jwtUtil.extractExpiration(jwt).toString();

            return new LoginResponseDto(jwt, expirationDate);

        } catch (BadCredentialsException e) {
            throw new NotAuthorizedException("Credenciales incorrectas.");
        }
    }
}
