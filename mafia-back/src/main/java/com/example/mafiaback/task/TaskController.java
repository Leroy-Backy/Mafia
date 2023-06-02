package com.example.mafiaback.task;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {
  private final TaskService taskService;
  
  @PostMapping
  @PreAuthorize("hasAuthority('ROLE_MANAGER')")
  public ResponseEntity<Integer> createTask(@NonNull @RequestBody TaskDto taskDto) {
    return ResponseEntity.ok(taskService.createTask(taskDto));
  }
  
  @PutMapping("/{id}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity updateTask(@NonNull @PathVariable Integer id, @RequestBody TaskDto taskDto) {
    taskService.updateTask(id, taskDto);
    return ResponseEntity.ok().build();
  }
  
  @GetMapping
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<List<TaskDto>> getAllTasksForUser() {
    return ResponseEntity.ok(taskService.getAllTasksForUser());
  }
  
  @GetMapping("/{id}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<TaskDto> getTaskById(@NonNull @PathVariable Integer id) {
    return ResponseEntity.ok(taskService.getTaskById(id));
  }
  
  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('ROLE_MANAGER')")
  public ResponseEntity deleteTask(@NonNull @PathVariable Integer id) {
    taskService.deleteTask(id);
    return ResponseEntity.ok().build();
  }
}
