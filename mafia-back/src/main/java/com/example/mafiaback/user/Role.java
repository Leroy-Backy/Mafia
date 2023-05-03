package com.example.mafiaback.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public enum Role {
  ROLE_GUARD,
  ROLE_MANAGER,
  ROLE_CLIENT;
  
  public List<GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(this.name()));
  }
}
