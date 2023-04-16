package com.example.mafiaback.security;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
  private String token;
  private String newPassword;
}
