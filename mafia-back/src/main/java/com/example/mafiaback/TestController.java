package com.example.mafiaback;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/api/test")
public class TestController {
  
  @GetMapping
  @PreAuthorize("hasAuthority('ROLE_MANAGER')")
  public ResponseEntity<String> test() {
    return ResponseEntity.ok("YEEES " + new Random().nextInt());
  }
}
