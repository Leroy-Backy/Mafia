package com.example.mafiaback.token;

import com.example.mafiaback.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String token;

  private LocalDateTime createdAt;

  private LocalDateTime expiresAt;
  
  @Enumerated(EnumType.STRING)
  private TokenType tokenType;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
}
