package com.example.mafiaback.guard;

import com.example.mafiaback.user.Role;
import com.example.mafiaback.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Guard extends User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  
  private Integer managerId;

  @Builder
  public Guard(Integer id, String firstName, String lastName, String email, String password, String phone, Role role, boolean enabled, Integer userId, Integer managerId) {
    super(userId, firstName, lastName, email, password, phone, role, enabled);
    this.id = id;
  }
}
