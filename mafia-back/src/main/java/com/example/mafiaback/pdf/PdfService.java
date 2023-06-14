package com.example.mafiaback.pdf;

import com.example.mafiaback.task.Task;
import com.example.mafiaback.task.TaskDto;
import com.example.mafiaback.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PdfService {
  
  private final SpringTemplateEngine templateEngine;
  private final PdfRepository pdfRepository;
  
  public File generatePdf(List<TaskDto> tasks, LocalDate from, LocalDate to) throws IOException {
    User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String html = templateEngine.process("pdfReport", getContext(tasks, currentUser, from, to));
    return renderPdf(html, currentUser);
  }
  
  private File renderPdf(String html, User user) throws IOException {
    String fileName = System.currentTimeMillis() + UUID.randomUUID().toString();
    File file = new File("/home/leroy/Folders/WSPA/Sem6/ProjektZespolowy/Mafia/mafia-back/src/main/resources/files/" + fileName + ".pdf");
    OutputStream outputStream = new FileOutputStream(file);
    ITextRenderer renderer = new ITextRenderer(20f * 4f / 3f, 20);
    renderer.setDocumentFromString(html);
    renderer.layout();
    renderer.createPDF(outputStream);
    outputStream.close();
    pdfRepository.save(new PDFEntity().builder().fileName(fileName).managerId(user.getId()).build());
    return file;
  }
  
  private Context getContext(List<TaskDto> tasks, User user, LocalDate from, LocalDate to) {
    Context context = new Context();
    
    context.setVariable("tasks", tasks);
    context.setVariable("manager", user.getFirstName() + " " + user.getLastName());
    context.setVariable("from", from);
    context.setVariable("to", to);
    return context;
  }
}
