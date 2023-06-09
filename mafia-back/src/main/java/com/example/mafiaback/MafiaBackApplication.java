package com.example.mafiaback;

import com.example.mafiaback.manager.Manager;
import com.example.mafiaback.manager.ManagerRepository;
import com.example.mafiaback.pdf.PdfService;
import com.example.mafiaback.task.TaskDto;
import com.example.mafiaback.task.TaskRepository;
import com.example.mafiaback.user.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.TimeZone;
import java.util.stream.Collectors;

@SpringBootApplication
public class MafiaBackApplication {

  public static void main(String[] args) {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    
    SpringApplication.run(MafiaBackApplication.class, args);
  }
  
  @Bean
  public CommandLineRunner loadData(ManagerRepository managerRepository, PasswordEncoder passwordEncoder) {
    return args -> {
      if(managerRepository.findByEmail("manager@gmail.com").isEmpty()) {
        Manager manager = Manager.builder()
            .firstName("Jan")
            .lastName("Kowalski")
            .city("Lublin")
            .district("Śródmieście")
            .role(Role.ROLE_MANAGER)
            .email("manager@gmail.com")
            .password(passwordEncoder.encode("manager"))
            .phone("111222333")
            .enabled(true)
            .build();

        managerRepository.save(manager);
      }
    };
  }
  
}
