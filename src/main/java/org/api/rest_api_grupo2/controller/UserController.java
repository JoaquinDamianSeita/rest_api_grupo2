package org.api.rest_api_grupo2.controller;

import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.api.rest_api_grupo2.dto.request.LoginRequest;
import org.api.rest_api_grupo2.dto.request.RegisterRequest;
import org.api.rest_api_grupo2.dto.request.UpdateRequest;
import org.api.rest_api_grupo2.dto.response.UserResponseDto;
import org.api.rest_api_grupo2.service.IUserService;
import org.api.rest_api_grupo2.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        return new ResponseEntity<>(userService.saveNewUser(request), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UpdateRequest request) throws BadRequestException {
        return new ResponseEntity<>(userService.updateUser(request), HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyInfo() throws BadRequestException {
        return ResponseEntity.ok(userService.getAuthenticatedUserInfo());
    }
}
