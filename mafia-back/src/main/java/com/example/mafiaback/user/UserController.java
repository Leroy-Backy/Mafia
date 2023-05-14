package com.example.mafiaback.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
  private final UserRepository userRepository;

  @GetMapping("/current")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<UserDto> getCurrent() {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    return ResponseEntity.ok(UserDto.fromUser(userRepository.findByEmail(email).get()));
  }
}
