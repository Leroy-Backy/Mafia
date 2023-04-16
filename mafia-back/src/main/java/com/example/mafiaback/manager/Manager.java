package com.example.mafiaback.manager;

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
public class Manager extends User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  
  private String city;
  private String district;

  @Builder
  public Manager(Integer userId, String firstName, String lastName, String email, String password, String phone, Role role, boolean enabled, Integer id, String city, String district) {
    super(userId, firstName, lastName, email, password, phone, role, enabled);
    this.id = id;
    this.city = city;
    this.district = district;
  }
}
