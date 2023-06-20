package com.example.mafiaback.user;

import com.example.mafiaback.token.Token;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity(name = "person")
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  
  private String firstName;
  private String lastName;
  
  @Column(unique = true, nullable = false)
  private String email;
  
  @Column(nullable = false)
  private String password;
  
  @Column(unique = true)
  private String phone;
  
  @Enumerated(EnumType.STRING)
  private Role role;
  
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
  private Set<Token> tokens = new HashSet<>();
  
  private boolean enabled;

  public User(Integer id, String firstName, String lastName, String email, String password, String phone, Role role, boolean enabled) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
    this.phone = phone;
    this.role = role;
    this.enabled = enabled;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return role.getAuthorities();
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }
}
