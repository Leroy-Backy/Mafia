package com.example.mafiaback.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
  List<Task> findByManagerIdOrderByCreatedAtDesc(Integer id);
  List<Task> findByManagerIdAndTaskStatusOrderByCreatedAtDesc(Integer id, TaskStatus status);
  List<Task> findByGuardIdOrderByCreatedAtDesc(Integer id);
  List<Task> findByGuardIdAndTaskStatusOrderByCreatedAtDesc(Integer id, TaskStatus status);
}
