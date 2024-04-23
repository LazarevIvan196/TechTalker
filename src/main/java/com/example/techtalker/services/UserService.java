package com.example.techtalker.services;


import com.example.techtalker.dto.UserDto;
import com.example.techtalker.entity.Role;
import com.example.techtalker.repositoryes.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.techtalker.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;


@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с именем: " + username + " не найден"));
    }

    @SuppressWarnings("unused")
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с ID: " + userId + " не найден"));
    }

    public User findUserByName(String name) {
        return userRepository.findUserByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с именем: " + name + " не найден"));
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public boolean saveUser(UserDto userDto) {
        if (userRepository.findUserByUsername(userDto.getUsername()).isPresent() ||
            userRepository.findUserByEmail(userDto.getEmail()).isPresent()) {

            return false;
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        user.setRoles(Collections.singleton(new Role(1L, "USER_ROLE")));
        userRepository.save(user);
        return true;
    }

    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с ID: " + userId + " не найден"));
        userRepository.deleteById(userId);
    }

}