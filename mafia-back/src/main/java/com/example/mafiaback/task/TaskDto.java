package com.example.mafiaback.task;

import com.example.mafiaback.point.PointDto;
import com.example.mafiaback.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TaskDto {
  public Integer id;
  private String name;
  private String description;
  private String resultDescription;
  private Timestamp createdAt;
  private TaskStatus taskStatus;
  private TaskType taskType;
  private UserDto manager;
  private UserDto guard;
  private PointDto point;
  
  public static TaskDto fromTask(Task task) {
    return TaskDto.builder()
        .id(task.getId())
        .name(task.getName())
        .description(task.getDescription())
        .resultDescription(task.getResultDescription())
        .createdAt(task.getCreatedAt())
        .taskStatus(task.getTaskStatus())
        .taskType(task.getTaskType())
        .manager(UserDto.fromManager(task.getManager()))
        .guard(UserDto.fromGuard(task.getGuard()))
        .point(PointDto.fromPoint(task.getPoint()))
        .build();
  }
  
  public static Task fromTaskDto(TaskDto taskDto) {
    return Task.builder()
        .id(taskDto.getId())
        .name(taskDto.getName())
        .description(taskDto.getDescription())
        .resultDescription(taskDto.getResultDescription())
        .taskStatus(taskDto.getTaskStatus())
        .taskType(taskDto.getTaskType())
        .build();
  }
}
