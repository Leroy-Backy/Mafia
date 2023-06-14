package com.example.mafiaback.pdf;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PdfRepository extends JpaRepository<PDFEntity, Integer> {
  List<PDFEntity> findByManagerIdOrderByIdDesc(Integer id);
}
