package com.example.mafiaback.pdf;

import com.example.mafiaback.task.TaskService;
import com.example.mafiaback.task.TaskStatus;
import com.example.mafiaback.user.User;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.xhtmlrenderer.util.IOUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
public class ReportController {
  private final PdfService pdfService;
  private final PdfRepository pdfRepository;
  private final TaskService taskService;

  @PostMapping
  @PreAuthorize("hasAuthority('ROLE_MANAGER')")
  public void generatePdf(
      @RequestParam String from,
      @RequestParam String to
  ) throws IOException {
    LocalDate startDate = LocalDate.parse(from, DateTimeFormatter.ISO_DATE);
    LocalDate endDate = LocalDate.parse(to, DateTimeFormatter.ISO_DATE);
    
    pdfService.generatePdf(taskService.getTasksForManagerByDateRange(startDate, endDate), startDate, endDate);
  }
  
  @GetMapping
  @PreAuthorize("hasAuthority('ROLE_MANAGER')")
  public ResponseEntity<List<String>> getReportsList() {
    User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return ResponseEntity.ok(pdfRepository.findByManagerIdOrderByIdDesc(currentUser.getId()).stream().map(PDFEntity::getFileName).collect(Collectors.toList()));
  }
  
  @PreAuthorize("permitAll()")
  @GetMapping(value = "/download/{fileName}", produces = {MediaType.APPLICATION_PDF_VALUE})
  public @ResponseBody byte[] downloadPdf(@PathVariable String fileName) {
    Path dest = Paths.get("/home/leroy/Folders/WSPA/Sem6/ProjektZespolowy/Mafia/mafia-back/src/main/resources/files/" + fileName + ".pdf");

    try {
      return IOUtils.toByteArray(dest.toUri());
    } catch (IOException e) {
      throw new RuntimeException("Can't download file");
    }
  }
}
