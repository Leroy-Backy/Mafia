package com.example.mafiaback.manager;

import com.example.mafiaback.errorhandling.MafiaForbiddenException;
import com.example.mafiaback.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.mafiaback.utils.SetterUtils.setIfNotNull;

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
      throw new MafiaForbiddenException("Current manager has no access to this entity");
    }
  }

  public void editManager(UserDto userDto) {
    Manager manager = getCurrentManager();

    setIfNotNull(userDto.getFirstName(), manager::setFirstName);
    setIfNotNull(userDto.getLastName(), manager::setLastName);
    setIfNotNull(userDto.getEmail(), manager::setEmail);
    setIfNotNull(userDto.getPhone(), manager::setPhone);
    setIfNotNull(userDto.getCity(), manager::setCity);
    setIfNotNull(userDto.getDistrict(), manager::setDistrict);

    managerRepository.save(manager);
  }
}
