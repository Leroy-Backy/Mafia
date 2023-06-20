package com.example.mafiaback.guard;

import com.example.mafiaback.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/guard")
public class GuardController {
  
  private final GuardService guardService;
  
  @PostMapping
  @PreAuthorize("hasAuthority('ROLE_MANAGER')")
  public ResponseEntity<Integer> createGuard(@RequestBody UserDto userDto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(guardService.createGuard(userDto));
  }
  
  @GetMapping
  @PreAuthorize("hasAuthority('ROLE_MANAGER')")
  public ResponseEntity<List<UserDto>> getManagersGuards() {
    return ResponseEntity.ok(guardService.getManagersGuards());
  }
  
  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('ROLE_MANAGER')")
  public ResponseEntity deleteGuard(@NonNull @PathVariable Integer id) {
    guardService.deleteGuard(id);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('ROLE_MANAGER')")
  public ResponseEntity editGuardAsManager(@NonNull @PathVariable Integer id, @RequestBody UserDto userDto) {
    guardService.editGuardAsManager(id, userDto);
    return ResponseEntity.ok().build();
  }

  @PutMapping
  @PreAuthorize("hasAuthority('ROLE_GUARD')")
  public ResponseEntity editGuardAsGuard(@RequestBody UserDto userDto) {
    guardService.editGuardAsGuard(userDto);
    return ResponseEntity.ok().build();
  }
}
