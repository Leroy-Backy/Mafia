package com.example.mafiaback.manager;

import com.example.mafiaback.errorhandling.MafiaForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ManagerService {
  
  private final ManagerRepository managerRepository;
  
  public Manager getCurrentManager() {
    String managerEmail = SecurityContextHolder.getContext().getAuthentication().getName();

    Optional<Manager> managerOptional = managerRepository.findByEmail(managerEmail);
    
    return managerOptional.get();
  }
  
  public void checkAccess(Integer managerId) {
    Manager manager = getCurrentManager();
    if(!manager.getId().equals(managerId)) {
      throw new MafiaForbiddenException("Current manager has no access to this guard");
    }
  }
}
