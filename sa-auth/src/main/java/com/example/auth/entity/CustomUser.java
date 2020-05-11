package com.example.auth.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomUser extends User {
    public CustomUser(UserEntity userEntity) {
        super(userEntity.getUsername(), userEntity.getPassword(), userEntity.getRoles().stream().map(roleEntity -> (GrantedAuthority) roleEntity::getName).collect(Collectors.toList()));
    }
}
