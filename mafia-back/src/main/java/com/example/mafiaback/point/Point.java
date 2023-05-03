package com.example.mafiaback.point;

import com.example.mafiaback.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Point {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;
  private Integer price;
  private Timestamp lastPaymentDate;
  @Column(precision = 10, scale = 6)
  private BigDecimal latitude;
  @Column(precision = 10, scale = 6)
  private BigDecimal longitude;
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "address_id")
  private Address address;
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "client_id")
  private User client;
  private Integer managerId;
}
