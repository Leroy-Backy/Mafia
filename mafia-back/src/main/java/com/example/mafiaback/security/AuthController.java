package com.example.mafiaback.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

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

  @PostMapping("/password/change/{email}")
  public ResponseEntity changePassword(@NonNull @PathVariable String email) {
    authService.makeChangePasswordRequestForController(email);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> auth(@RequestBody AuthRequest authRequest) {
    return ResponseEntity.ok(authService.authenticate(authRequest));
  }
}
