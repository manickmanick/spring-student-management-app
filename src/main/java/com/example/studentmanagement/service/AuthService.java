package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.LoginRequest;
import com.example.studentmanagement.dto.RegisterRequest;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.exception.UsernameAlreadyExistsException;
import com.example.studentmanagement.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.studentmanagement.security.JwtService;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public String register(RegisterRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException(request.getUsername());
        }

        User user = new User();

        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        return "User Registered Successfully";
    }

    public String login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        return jwtService.generateToken(user);
    }
}