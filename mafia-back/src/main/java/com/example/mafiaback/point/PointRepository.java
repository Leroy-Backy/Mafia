package com.example.mafiaback.point;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<Point, Integer> {
  List<Point> findByManagerId(Integer id);
}
