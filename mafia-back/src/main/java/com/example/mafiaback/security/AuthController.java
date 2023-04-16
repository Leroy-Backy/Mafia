package com.example.mafiaback.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  
  private final AuthService authService;
  
  @PostMapping("/password/change")
  public ResponseEntity changePassword(@RequestBody ChangePasswordRequest request) {
    authService.changePassword(request);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> auth(@RequestBody AuthRequest authRequest) {
    return ResponseEntity.ok(authService.authenticate(authRequest));
  }
}
