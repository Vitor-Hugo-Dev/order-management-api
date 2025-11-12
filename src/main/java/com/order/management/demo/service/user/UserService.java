package com.order.management.demo.service.user;

import com.order.management.demo.config.exceptionHandler.ResourceNotFoundException;
import com.order.management.demo.dto.user.UserDTO;
import com.order.management.demo.model.User;
import com.order.management.demo.model.enuns.Role;
import com.order.management.demo.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDTO updateUser(User currentUser, UUID userIdToUpdate, UserDTO request) {

        User userToUpdate = userRepository.findById(userIdToUpdate)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "id", userIdToUpdate));

        if (currentUser.getRole() == Role.ADMIN) {
            adminUpdate(userToUpdate, request);
        } else if (currentUser.getRole() == Role.USER) {
            userUpdate(currentUser, userToUpdate, request);
        }

        User savedUser = userRepository.save(userToUpdate);

        return UserDTO.builder()
                .email(savedUser.getEmail())
                .name(savedUser.getName())
                .role(savedUser.getRole())
                .build();
    }

    private void adminUpdate(User userToUpdate, UserDTO request) {

        userToUpdate.setEmail(request.getEmail());
        userToUpdate.setName(request.getName());
        userToUpdate.setRole(request.getRole());
        userToUpdate.setPassword(passwordEncoder.encode(request.getPassword()));
    }

    private void userUpdate(User currentUser, User userToUpdate, UserDTO request) {
        if (!currentUser.getId().equals(userToUpdate.getId())) {
            throw new AccessDeniedException("Usuário não tem permissão para alterar este perfil.");
        }

        userToUpdate.setEmail(request.getEmail());
        userToUpdate.setName(request.getName());
        userToUpdate.setPassword(passwordEncoder.encode(request.getPassword()));
    }

    public List<UserDTO> listUsers() {
        List<User> users = userRepository.findAll();
        return userWrapper(users);
    }

    private List<UserDTO> userWrapper(List<User> userList) {
        return userList.stream().map(
                user -> UserDTO.builder()
                        .name(user.getName())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .id(user.getId())
                        .build()
        ).collect(Collectors.toList());
    }
}