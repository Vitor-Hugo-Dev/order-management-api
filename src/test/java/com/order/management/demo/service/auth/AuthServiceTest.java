package com.order.management.demo.service.auth;

import com.order.management.demo.config.JwtServiceConfig;
import com.order.management.demo.dto.auth.AuthResponse;
import com.order.management.demo.dto.auth.LoginRequest;
import com.order.management.demo.dto.user.UserDTO;
import com.order.management.demo.model.User;
import com.order.management.demo.model.enuns.Role;
import com.order.management.demo.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do service de Autenticação (AuthService)")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtServiceConfig jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private UserDTO userRequestDTO;
    private User mockUser;

    @BeforeEach
    void setUp() {
        userRequestDTO = UserDTO.builder()
                .name("Test User")
                .email("test@user.com")
                .password("password123")
                .build();

        mockUser = User.builder()
                .name("Test User")
                .email("test@user.com")
                .role(Role.USER)
                .build();
        mockUser.setId(UUID.randomUUID());
    }

    @Test
    @DisplayName("Deve registrar um novo usuário com a role 'USER' quando o email for novo")
    void register_QuandoEmailForNovo_DeveRegistrarUsuarioComoUSER() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_password");
        when(jwtService.generateToken(any(User.class))).thenReturn("mock.jwt.token");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User userToSave = invocation.getArgument(0);
            userToSave.setId(UUID.randomUUID());
            return userToSave;
        });

        AuthResponse response = authService.register(userRequestDTO);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("mock.jwt.token");
        assertThat(response.getEmail()).isEqualTo(userRequestDTO.getEmail());

        verify(userRepository).save(argThat(savedUser ->
                savedUser.getPassword().equals("hashed_password") &&
                        savedUser.getRole() == Role.USER
        ));
    }

    @Test
    @DisplayName("Deve lançar 'IllegalArgumentException' quando o email já existir")
    void register_QuandoEmailJaExiste_DeveLancarExcecao() {
        when(userRepository.findByEmail("test@user.com")).thenReturn(Optional.of(mockUser));

        assertThatThrownBy(() -> authService.register(userRequestDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email já cadastrado");

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve retornar um token JWT quando as credenciais de login estiverem corretas")
    void login_QuandoCredenciaisSaoValidas_DeveRetornarToken() {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@user.com")
                .password("password123")
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByEmail("test@user.com")).thenReturn(Optional.of(mockUser));
        when(jwtService.generateToken(any(User.class))).thenReturn("mock.jwt.token");

        AuthResponse response = authService.login(loginRequest);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("mock.jwt.token");
        assertThat(response.getId()).isEqualTo(mockUser.getId());
    }
}