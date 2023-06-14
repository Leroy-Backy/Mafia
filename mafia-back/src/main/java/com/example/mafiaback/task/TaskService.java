package com.example.mafiaback.task;

import com.example.mafiaback.errorhandling.MafiaEntityNotFoundException;
import com.example.mafiaback.errorhandling.MafiaForbiddenException;
import com.example.mafiaback.guard.Guard;
import com.example.mafiaback.guard.GuardRepository;
import com.example.mafiaback.manager.Manager;
import com.example.mafiaback.manager.ManagerService;
import com.example.mafiaback.point.Point;
import com.example.mafiaback.point.PointRepository;
import com.example.mafiaback.user.Role;
import com.example.mafiaback.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.mafiaback.utils.SetterUtils.setIfNotNull;

@Service
@RequiredArgsConstructor
public class TaskService {
  private final TaskRepository taskRepository;
  private final GuardRepository guardRepository;
  private final ManagerService managerService;
  private final PointRepository pointRepository;
  
  public List<TaskDto> getAllTasksForUser(TaskStatus status) {
    User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    
    List<Task> tasks;
    
    if(Role.isManager(currentUser.getRole())) {
      if(TaskStatus.ALL.equals(status)) {
        tasks = taskRepository.findByManagerIdOrderByCreatedAtDesc(currentUser.getId());
      } else {
        tasks = taskRepository.findByManagerIdAndTaskStatusOrderByCreatedAtDesc(currentUser.getId(), status);
      }
    } else {
      if(TaskStatus.ALL.equals(status)) {
        tasks = taskRepository.findByGuardIdOrderByCreatedAtDesc(currentUser.getId());
      } else {
        tasks = taskRepository.findByGuardIdAndTaskStatusOrderByCreatedAtDesc(currentUser.getId(), status);
      }
    }
    
    return tasks.stream().map(TaskDto::fromTask).toList();
  }
  
  public List<TaskDto> getTasksForManagerByDateRange(LocalDate from, LocalDate to) {
    User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    
    List<Task> tasks = taskRepository.findByManagerIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThanEqualOrderByCreatedAtDesc(currentUser.getId(), Timestamp.valueOf(from.atStartOfDay()), Timestamp.valueOf(to.atStartOfDay()));
    
    return tasks.stream().map(TaskDto::fromTask).toList();
  }
  
  @Transactional
  public Integer createTask(TaskDto taskDto) {
    Optional<Guard> guardOptional = guardRepository.findById(taskDto.getGuard().getId());
    
    if(guardOptional.isEmpty()) {
      throw new MafiaEntityNotFoundException("Guard with id: " + taskDto.getGuard().getId() + " does not exist");
    }
    
    Guard guard = guardOptional.get();
    managerService.checkAccess(guard.getManagerId());

    Optional<Point> pointOptional = pointRepository.findById(taskDto.getPoint().getId());

    if(pointOptional.isEmpty()) {
      throw new MafiaEntityNotFoundException("Point with id: " + taskDto.getPoint().getId() + " does not exist");
    }

    Point point = pointOptional.get();
    managerService.checkAccess(point.getManagerId());

    Manager currentManager = (Manager) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    
    Task task = TaskDto.fromTaskDto(taskDto);
    task.setManager(currentManager);
    task.setGuard(guard);
    task.setPoint(point);
    
    taskRepository.save(task);
    
    return task.getId();
  }

  @Transactional
  public void updateTask(Integer id, TaskDto taskDto) {
    User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    boolean isManager = Role.isManager(currentUser.getRole());
  
    Optional<Task> taskOptional = taskRepository.findById(id);
    if(taskOptional.isEmpty()) {
      throw new MafiaEntityNotFoundException("Task with id: " + id + " does not exist");
    }
    Task task = taskOptional.get();

    if (
        (isManager && !task.getManager().getId().equals(currentUser.getId())) ||
        (!isManager && !task.getGuard().getId().equals(currentUser.getId()))
    ) {
      throw new MafiaForbiddenException("Current user has no access to this entity");
    }
    
    setIfNotNull(taskDto.getTaskStatus(), task::setTaskStatus);
    setIfNotNull(taskDto.getResultDescription(), task::setResultDescription);
    if(isManager) {
      setIfNotNull(taskDto.getName(), task::setName);
      setIfNotNull(taskDto.getDescription(), task::setDescription);
      setIfNotNull(taskDto.getTaskType(), task::setTaskType);
      if(taskDto.getGuard() != null && taskDto.getGuard().getId() != null) {
        Optional<Guard> guardOptional = guardRepository.findById(taskDto.getGuard().getId());
        guardOptional.ifPresent(task::setGuard);
      }
      if(taskDto.getPoint() != null && taskDto.getPoint().getId() != null) {
        Optional<Point> pointOptional = pointRepository.findById(taskDto.getPoint().getId());
        pointOptional.ifPresent(task::setPoint);
      }
    }
    
    taskRepository.save(task);
  }
  
  public void deleteTask(Integer id) {
    Optional<Task> taskOptional = taskRepository.findById(id);
    
    if(taskOptional.isEmpty()) {
      throw new MafiaEntityNotFoundException("Task with id: " + id + " does not exist");
    }
    
    managerService.checkAccess(taskOptional.get().getManager().getId());
    
    taskRepository.delete(taskOptional.get());
  }

  public TaskDto getTaskById(Integer id) {
    Optional<Task> taskOptional = taskRepository.findById(id);
    
    if(taskOptional.isEmpty()) {
      throw new MafiaEntityNotFoundException("Task with id: " + id + " not found");
    }
    User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    
    Task task = taskOptional.get();

    if ((Role.isManager(currentUser.getRole()) && !task.getManager().getId().equals(currentUser.getId())) ||
        (!Role.isManager(currentUser.getRole()) && !task.getGuard().getId().equals(currentUser.getId()))
    ) {
      throw new MafiaForbiddenException("Current user has no access to this entity");
    }

    return TaskDto.fromTask(task);
  }
}
