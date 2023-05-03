package com.example.mafiaback.manager;

import com.example.mafiaback.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manager")
public class ManagerController {
  private final ManagerService managerService;
  
  @PutMapping
  @PreAuthorize("hasAuthority('ROLE_MANAGER')")
  public ResponseEntity editManager(@RequestBody UserDto userDto) {
    managerService.editManager(userDto);
    return ResponseEntity.ok().build();
  }
}
