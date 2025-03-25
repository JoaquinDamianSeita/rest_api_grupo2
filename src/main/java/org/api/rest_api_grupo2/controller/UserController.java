package org.api.rest_api_grupo2.controller;

import jakarta.validation.Valid;
import org.api.rest_api_grupo2.dto.request.RegisterRequest;
import org.api.rest_api_grupo2.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        return new ResponseEntity<>(userService.saveNewUser(request), HttpStatus.OK);
    }
}
