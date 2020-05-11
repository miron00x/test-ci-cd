package com.example.auth.controller;

import com.example.auth.entity.UserEntity;
import com.example.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.security.Principal;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public Flux<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @GetMapping("/users/current")
    public Principal getCurrentUser(Principal principal) {
        return principal;
    }

    @GetMapping("/testHealth")
    public String testHealth() {
        return "OK";
    }
}
