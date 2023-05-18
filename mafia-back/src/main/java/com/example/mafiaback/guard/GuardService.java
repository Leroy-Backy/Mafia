package com.example.mafiaback.guard;

import com.example.mafiaback.errorhandling.MafiaEntityNotFoundException;
import com.example.mafiaback.manager.Manager;
import com.example.mafiaback.manager.ManagerService;
import com.example.mafiaback.point.Point;
import com.example.mafiaback.point.PointDto;
import com.example.mafiaback.security.AuthService;
import com.example.mafiaback.user.Role;
import com.example.mafiaback.user.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.mafiaback.utils.SetterUtils.*;

@Service
@AllArgsConstructor
public class GuardService {
  
  private final GuardRepository guardRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthService authService;
  private final ManagerService managerService;
  
  public void deleteGuard(Integer id) {
    Optional<Guard> guardOptional = guardRepository.findById(id);
    
    if(guardOptional.isEmpty()) {
      throw new MafiaEntityNotFoundException("Guard with id: " + id + " does not exist");
    }
    
    managerService.checkAccess(guardOptional.get().getManagerId());
    
    guardRepository.delete(guardOptional.get());
  }
  
  public List<UserDto> getManagersGuards() {
    Manager manager = managerService.getCurrentManager();
    
    List<Guard> guards = guardRepository.findByManagerId(manager.getId());
    
    return guards.stream().map(UserDto::fromGuard).collect(Collectors.toList());
  }
  
  @Transactional
  public Integer createGuard(UserDto userDto) {

    Optional<Guard> guardOptional = guardRepository.findByEmail(userDto.getEmail());

    Guard guard;
    
    if(guardOptional.isPresent() && !guardOptional.get().isEnabled()){
      guard = guardOptional.get();
    } else if(guardOptional.isPresent()) {
      throw new RuntimeException("User with email: " + userDto.getEmail() + " already exists");
    } else {
      guard = new Guard();
    }
    
    guard.setFirstName(userDto.getFirstName());
    guard.setLastName(userDto.getLastName());
    guard.setEmail(userDto.getEmail());
    guard.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
    guard.setRole(Role.ROLE_GUARD);
    guard.setManagerId(managerService.getCurrentManager().getId());
    guard.setPhone(userDto.getPhone());
    guard.setEnabled(false);
    
    guardRepository.save(guard);

    authService.makeChangePasswordRequest(guard, userDto.getEmail(), userDto.getFirstName());
    
    return guard.getId();
  }
  
  public Integer editGuardAsManager(Integer id, UserDto userDto) {
    Optional<Guard> guardOptional = guardRepository.findById(id);

    if(guardOptional.isEmpty()) {
      throw new MafiaEntityNotFoundException("Guard with id: " + id + " does not exist");
    }
    
    Guard guard = guardOptional.get();

    managerService.checkAccess(guard.getManagerId());

    setIfNotNull(userDto.getFirstName(), guard::setFirstName);
    setIfNotNull(userDto.getLastName(), guard::setLastName);
    setIfNotNull(userDto.getEmail(), guard::setEmail);
    setIfNotNull(userDto.getManagerId(), guard::setManagerId);
    setIfNotNull(userDto.getPhone(), guard::setPhone);

    guardRepository.save(guard);

    return guard.getId();
  }

  public Integer editGuardAsGuard(UserDto userDto) {
    String guardEmail = SecurityContextHolder.getContext().getAuthentication().getName();
    
    Guard guard = guardRepository.findByEmail(guardEmail).get();

    setIfNotNull(userDto.getFirstName(), guard::setFirstName);
    setIfNotNull(userDto.getLastName(), guard::setLastName);
    setIfNotNull(userDto.getEmail(), guard::setEmail);
    setIfNotNull(userDto.getPhone(), guard::setPhone);

    guardRepository.save(guard);

    return guard.getId();
  }
}
