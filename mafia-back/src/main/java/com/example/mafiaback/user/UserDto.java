package com.example.mafiaback.user;

import com.example.mafiaback.guard.Guard;
import com.example.mafiaback.manager.Manager;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
  private Integer id;
  private String firstName;
  private String lastName;
  private String email;
  private String phone;
  private Integer managerId;
  private String city;
  private String district;
  private Role role;
  
  public static UserDto fromUser(User user) {
    return UserDto.builder()
        .id(user.getId())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .email(user.getEmail())
        .phone(user.getPhone())
        .role(user.getRole())
        .build();
  }
  
  public static UserDto fromGuard(Guard guard) {
    return UserDto.builder()
        .id(guard.getId())
        .firstName(guard.getFirstName())
        .lastName(guard.getLastName())
        .email(guard.getEmail())
        .phone(guard.getPhone())
        .managerId(guard.getManagerId())
        .build();
  }

  public static UserDto fromManager(Manager manager) {
    return UserDto.builder()
        .id(manager.getId())
        .firstName(manager.getFirstName())
        .lastName(manager.getLastName())
        .email(manager.getEmail())
        .phone(manager.getPhone())
        .city(manager.getCity())
        .district(manager.getDistrict())
        .build();
  }
}
