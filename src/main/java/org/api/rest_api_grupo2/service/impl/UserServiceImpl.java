package org.api.rest_api_grupo2.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.apache.coyote.BadRequestException;
import org.api.rest_api_grupo2.dto.request.LoginRequest;
import org.api.rest_api_grupo2.dto.request.RegisterRequest;
import org.api.rest_api_grupo2.dto.request.UpdateRequest;
import org.api.rest_api_grupo2.dto.response.LoginResponseDto;
import org.api.rest_api_grupo2.dto.response.MessageResponseDto;
import org.api.rest_api_grupo2.dto.response.UserResponseDto;
import org.api.rest_api_grupo2.exceptions.NotAuthorizedException;
import org.api.rest_api_grupo2.exceptions.NotFoundException;
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
import org.springframework.security.core.context.SecurityContextHolder;
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
        List<Optional<User>> existingUsers = userRepository.findByEmailOrUsername(request.getEmail(), request.getUsername());

        if (!existingUsers.isEmpty()) {
            throw new UnprocessableEntityException("El email o el username ya está en uso.");
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

    @Override
    public MessageResponseDto updateUser(UpdateRequest request) throws BadRequestException {
        User user = getAutheticatedUser();

        if (request.getEmail() != null) {
            Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
            if (existingUser.isPresent() && !existingUser.get().getId().equals(user.getId())) {
                throw new UnprocessableEntityException("El email ya está en uso.");
            }
            user.setEmail(request.getEmail());
        }

        if (request.getRoleId() != null) {
            Optional<Role> role = roleRepository.findById(request.getRoleId());
            if (role.isEmpty()) {
                throw new UnprocessableEntityException("El rol no existe.");
            }
            user.setRole(role.get());
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setAddress(request.getAddress());
        user.setBiography(request.getBiography());

        userRepository.save(user);

        return new MessageResponseDto("Usuario actualizado con éxito.");
    }

    @Override
    public UserResponseDto getAuthenticatedUserInfo() throws BadRequestException {
        User user = getAutheticatedUser();

        UserResponseDto response = new UserResponseDto();
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setAddress(user.getAddress());
        response.setBiography(user.getBiography());
        response.setRoleName(user.getRole().getName());
        response.setUserId(user.getId());

        return response;
    }

    @Override
    public User validateAuthorizedUser(Long userId) {
        Authentication authentication;
        authentication = SecurityContextHolder.getContext().getAuthentication();
        User userAuthenticated = (User) authentication.getPrincipal();

        if (!Objects.equals(userAuthenticated.getId(), userId)) {
            throw new NotAuthorizedException("No tienes permiso para acceder a este recurso.");
        }

        return userAuthenticated;
    }

    @Override
    public User getAutheticatedUser() throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User){
            return (User) authentication.getPrincipal();
        }
        throw new BadRequestException("No hay un usuario autenticado");
    }
}
