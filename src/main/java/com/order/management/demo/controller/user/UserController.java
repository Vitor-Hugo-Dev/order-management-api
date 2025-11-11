package com.order.management.demo.controller.user;

import com.order.management.demo.dto.user.UserDTO;
import com.order.management.demo.model.User;
import com.order.management.demo.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PutMapping("/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDTO> updateUser(@AuthenticationPrincipal User currentUser,
                                              @PathVariable UUID userId, @Valid @RequestBody UserDTO request) {
        UserDTO updatedUser = userService.updateUser(currentUser, userId, request);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/list-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers(@AuthenticationPrincipal User currentUser) {
        List<UserDTO> users = userService.listUsers();
        return ResponseEntity.ok(users);

    }
}