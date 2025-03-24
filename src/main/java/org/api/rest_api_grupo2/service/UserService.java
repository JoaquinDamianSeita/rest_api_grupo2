package org.api.rest_api_grupo2.service;

import org.api.rest_api_grupo2.model.User;
import org.api.rest_api_grupo2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
  
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}


