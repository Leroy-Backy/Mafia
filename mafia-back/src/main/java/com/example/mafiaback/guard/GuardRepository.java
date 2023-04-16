package com.example.mafiaback.guard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuardRepository extends JpaRepository<Guard, Integer> {
  Optional<Guard> findByEmail(String email);
  
  List<Guard> findByManagerId(Integer id);
}
