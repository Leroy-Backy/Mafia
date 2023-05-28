package com.example.mafiaback.task;

import com.example.mafiaback.guard.Guard;
import com.example.mafiaback.manager.Manager;
import com.example.mafiaback.point.Point;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  
  private String name;
  
  @Column(length = 65535)
  private String description;

  @Column(length = 65535)
  private String resultDescription;
  
  @CreationTimestamp
  private Timestamp createdAt;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "point_id")
  private Point point;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "guard_id")
  private Guard guard;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "manager_id")
  private Manager manager;
  
  @Enumerated(EnumType.STRING)
  private TaskStatus taskStatus;
  
  @Enumerated(EnumType.STRING)
  private TaskType taskType;
}
