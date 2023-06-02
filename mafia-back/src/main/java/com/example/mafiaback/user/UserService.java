package com.example.mafiaback.user;

import com.example.mafiaback.errorhandling.MafiaEntityNotFoundException;
import com.example.mafiaback.errorhandling.MafiaForbiddenException;
import com.example.mafiaback.guard.Guard;
import com.example.mafiaback.manager.Manager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
  
  private final UserRepository userRepository;
  
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }
  
  public UserDto getCurrent() {
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if(user.getRole().equals(Role.ROLE_MANAGER)) {
      return UserDto.fromManager((Manager) user);
    } else {
      return UserDto.fromGuard((Guard) user);
    }
  }
  
  public UserDto getUserById(Integer id) {
    User currentUuser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    
    Optional<User> userOptional = userRepository.findById(id);
    if(userOptional.isEmpty()) {
      throw new MafiaEntityNotFoundException("User with id: " + id + " does not exist");
    }
    
    User user = userOptional.get();

    if(user.getRole().equals(Role.ROLE_MANAGER)) {
      return UserDto.fromManager((Manager) user);
    } else {
      return UserDto.fromGuard((Guard) user);
    }
  }
}
