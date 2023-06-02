package com.example.mafiaback.point;

import com.example.mafiaback.errorhandling.MafiaEntityNotFoundException;
import com.example.mafiaback.manager.ManagerService;
import com.example.mafiaback.user.Role;
import com.example.mafiaback.user.User;
import com.example.mafiaback.user.UserDto;
import com.example.mafiaback.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.mafiaback.utils.SetterUtils.setIfNotNull;

@Service
@RequiredArgsConstructor
public class PointService {
  private final PointRepository pointRepository;
  private final UserRepository userRepository;
  private final ManagerService managerService;
  private final PasswordEncoder passwordEncoder;
  
  public List<PointDto> getPoints() {
    Integer managerId = managerService.getCurrentManager().getId();
    return pointRepository.findByManagerId(managerId).stream().map(PointDto::fromPoint).toList();
  }
  
  public PointDto getPointById(Integer id) {
    Optional<Point> pointOptional = pointRepository.findById(id);
    
    if(pointOptional.isEmpty()) {
      throw new MafiaEntityNotFoundException("Point with id: " + id + " not found");
    }
    
    return PointDto.fromPoint(pointOptional.get());
  }
  
  @Transactional
  public Integer createPoint(PointDto pointDto) {
    UserDto clientDto = pointDto.getClient();

    Optional<User> userOptional = userRepository.findByEmail(clientDto.getEmail());
    
    User client;
    
    if (userOptional.isPresent() && userOptional.get().isEnabled()) {
      client = userOptional.get();
    } else {
      client = new User();
      client.setEnabled(false);
      client.setEmail(clientDto.getEmail());
      client.setRole(Role.ROLE_CLIENT);
      client.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
      client.setFirstName(clientDto.getFirstName());
      client.setLastName(clientDto.getLastName());
      client.setPhone(clientDto.getPhone());
    }
    
    Point point = PointDto.fromPointDto(pointDto);
    point.setClient(client);
    point.setManagerId(managerService.getCurrentManager().getId());
    
    pointRepository.save(point);
    
    return point.getId();
  } 
  
  @Transactional
  public void updatePoint(Integer id, PointDto pointDto) {
    Optional<Point> pointOptional = pointRepository.findById(id);

    if(pointOptional.isEmpty()) {
      throw new MafiaEntityNotFoundException("Point with id: " + id + " does not exist");
    }

    Point point = pointOptional.get();

    managerService.checkAccess(point.getManagerId());

    setIfNotNull(pointDto.getName(), point::setName);
    setIfNotNull(pointDto.getPrice(), point::setPrice);
    setIfNotNull(pointDto.getLastPaymentDate(), point::setLastPaymentDate);
    setIfNotNull(pointDto.getLatitude(), point::setLatitude);
    setIfNotNull(pointDto.getLongitude(), point::setLongitude);
    setIfNotNull(pointDto.getManagerId(), point::setManagerId);
    if(pointDto.getAddress() != null) {
      point.setAddress(AddressDto.fromAddressDto(pointDto.getAddress()));
    }
    if(pointDto.getClient() != null) {
      UserDto clientDto = pointDto.getClient();
      
      setIfNotNull(clientDto.getEmail(), point.getClient()::setEmail);
      setIfNotNull(clientDto.getPhone(), point.getClient()::setPhone);
      setIfNotNull(clientDto.getFirstName(), point.getClient()::setFirstName);
      setIfNotNull(clientDto.getLastName(), point.getClient()::setLastName);
    }

    pointRepository.save(point);
  }
  
  public void deletePoint(Integer id) {
    Optional<Point> pointOptional = pointRepository.findById(id);

    if(pointOptional.isEmpty()) {
      throw new MafiaEntityNotFoundException("Point with id: " + id + " does not exist");
    }

    managerService.checkAccess(pointOptional.get().getManagerId());
    
    pointRepository.delete(pointOptional.get());
  }
}