package com.example.mafiaback.point;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/point")
@RequiredArgsConstructor
public class PointController {
  private final PointService pointService;
  
  @GetMapping
  @PreAuthorize("hasAuthority('ROLE_MANAGER')")
  public ResponseEntity<List<PointDto>> getAllPoints() {
    return ResponseEntity.ok(pointService.getPoints());
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('ROLE_MANAGER')")
  public ResponseEntity<PointDto> getPointById(@NonNull @PathVariable Integer id) {
    return ResponseEntity.ok(pointService.getPointById(id));
  }


  @PostMapping
  @PreAuthorize("hasAuthority('ROLE_MANAGER')")
  public ResponseEntity<Integer> createPoint(@RequestBody PointDto pointDto) {
    return ResponseEntity.ok(pointService.createPoint(pointDto));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('ROLE_MANAGER')")
  public ResponseEntity editPoint(@NonNull @PathVariable Integer id, @RequestBody PointDto pointDto) {
    pointService.updatePoint(id, pointDto);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('ROLE_MANAGER')")
  public ResponseEntity deleteGuard(@NonNull @PathVariable Integer id) {
    pointService.deletePoint(id);
    return ResponseEntity.ok().build();
  }
}
