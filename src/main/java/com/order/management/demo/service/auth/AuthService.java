package com.order.management.demo.service.auth;

import com.order.management.demo.config.JwtServiceConfig;
import com.order.management.demo.dto.auth.AuthResponse;
import com.order.management.demo.dto.auth.LoginRequest;
import com.order.management.demo.dto.user.UserDTO;
import com.order.management.demo.model.Enum.Role;
import com.order.management.demo.model.User;
import com.order.management.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtServiceConfig jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public AuthResponse register(UserDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(hashedPassword)
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);

        String jwtToken = jwtService.generateToken(savedUser);

        return AuthResponse.builder()
                .token(jwtToken)
                .email(savedUser.getEmail())
                .name(savedUser.getName())
                .id(savedUser.getId())
                .build();
    }

    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalStateException("Usuário não encontrado após autenticação"));

        String jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .name(user.getName())
                .id(user.getId())
                .build();
    }
}
