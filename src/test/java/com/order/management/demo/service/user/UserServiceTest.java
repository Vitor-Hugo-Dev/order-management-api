package com.order.management.demo.service.user;

import com.order.management.demo.config.exceptionHandler.ResourceNotFoundException;
import com.order.management.demo.dto.user.UserDTO;
import com.order.management.demo.model.User;
import com.order.management.demo.model.enuns.Role;
import com.order.management.demo.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do service de Usuário (UserService)")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Deve permitir que um ADMIN altere a role de outro usuário")
    void updateUser_QuandoUsuarioForAdmin_DeveAtualizarARole() {
        UUID adminId = UUID.randomUUID();
        UUID userIdToUpdate = UUID.randomUUID();

        User adminUser = User.builder().role(Role.ADMIN).build();
        adminUser.setId(adminId);

        User userToUpdate = User.builder().role(Role.USER).name("Old Name").build();
        userToUpdate.setId(userIdToUpdate);

        UserDTO request = UserDTO.builder()
                .name("New Name")
                .email("new@email.com")
                .password("newPass")
                .role(Role.ADMIN)
                .build();

        when(userRepository.findById(userIdToUpdate)).thenReturn(Optional.of(userToUpdate));
        when(passwordEncoder.encode("newPass")).thenReturn("hashedNewPass");
        when(userRepository.save(any(User.class))).thenReturn(userToUpdate);

        userService.updateUser(adminUser, userIdToUpdate, request);

        verify(userRepository).save(argThat(user ->
                user.getName().equals("New Name") && user.getRole() == Role.ADMIN && user.getPassword().equals("hashedNewPass")));
    }

    @Test
    @DisplayName("Deve permitir que um USER altere seus dados, mas NãO sua própria role")
    void updateUser_QuandoUsuarioForDono_DeveAtualizarNomeMasNaoARole() {
        UUID userId = UUID.randomUUID();
        User currentUser = User.builder().role(Role.USER).build();
        currentUser.setId(userId);

        UserDTO request = UserDTO.builder()
                .name("New Name")
                .email("new@email.com")
                .password("newPass")
                .role(Role.ADMIN)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(currentUser));
        when(passwordEncoder.encode("newPass")).thenReturn("hashedNewPass");
        when(userRepository.save(any(User.class))).thenReturn(currentUser);

        userService.updateUser(currentUser, userId, request);

        verify(userRepository).save(argThat(user -> user.getName().equals("New Name") && user.getRole() == Role.USER));
    }

    @Test
    @DisplayName("Deve lançar 'AccessDeniedException' se um USER tentar alterar outro usuário")
    void updateUser_QuandoUsuarioNaoForDono_DeveLancarAccessDenied() {
        UUID currentUserId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();

        User currentUser = User.builder().role(Role.USER).build();
        currentUser.setId(currentUserId);

        User otherUser = User.builder().role(Role.USER).build();
        otherUser.setId(otherUserId);

        UserDTO request = UserDTO.builder().name("Hacker").build();

        when(userRepository.findById(otherUserId)).thenReturn(Optional.of(otherUser));

        assertThatThrownBy(() -> userService.updateUser(currentUser, otherUserId, request))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("Usuário não tem permissão para alterar este perfil.");

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar 'ResourceNotFoundException' se o usuário a ser atualizado não existir")
    void updateUser_QuandoUsuarioNaoEncontrado_DeveLancarResourceNotFound() {
        UUID adminId = UUID.randomUUID();
        UUID missingUserId = UUID.randomUUID();
        User adminUser = User.builder().role(Role.ADMIN).build();
        adminUser.setId(adminId);
        UserDTO request = UserDTO.builder().name("Test").build();

        when(userRepository.findById(missingUserId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(adminUser, missingUserId, request))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}