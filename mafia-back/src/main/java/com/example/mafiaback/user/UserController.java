package com.example.mafiaback.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @GetMapping("/current")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<UserDto> getCurrent() {
    return ResponseEntity.ok(userService.getCurrent());
  }
  
  @GetMapping("/{userId}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<UserDto> getUserById(@NonNull @PathVariable Integer userId) {
    return ResponseEntity.ok(userService.getUserById(userId));
  }
}
