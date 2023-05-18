package com.example.mafiaback.security;

import com.example.mafiaback.errorhandling.MafiaEntityNotFoundException;
import com.example.mafiaback.mail.EmailService;
import com.example.mafiaback.token.Token;
import com.example.mafiaback.token.TokenRepository;
import com.example.mafiaback.token.TokenType;
import com.example.mafiaback.user.User;
import com.example.mafiaback.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authManager;
  private final TokenRepository tokenRepository;
  private final EmailService emailService;

  @Value("${mafia.mail.change-password.link}")
  private String activateLink;
  @Value("${mafia.mail.change-password.minutes}")
  private Integer activationTokenExpirationMinutes;

  @Transactional
  public void changePassword(ChangePasswordRequest request) {
    Optional<Token> token = tokenRepository.findByTokenAndTokenType(request.getToken(), TokenType.CHANGE_PASSWORD);
    
    if(token.isEmpty()) {
      throw new MafiaEntityNotFoundException("Change password token not found");
    }

    LocalDateTime expiredAt = token.get().getExpiresAt();

    if(expiredAt.isBefore(LocalDateTime.now()))
      throw new IllegalStateException("token expired");

    User user = token.get().getUser();

    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    user.setEnabled(true);

    userRepository.save(user);
    
    tokenRepository.delete(token.get());
  }
  
  public void makeChangePasswordRequestForController(String email) {
    Optional<User> userOptional = userRepository.findByEmail(email);
    
    if(userOptional.isEmpty()) {
      throw new MafiaEntityNotFoundException("User with email " + email + " not found");
    }
    
    User user = userOptional.get();
    
    makeChangePasswordRequest(user, user.getEmail(), user.getFirstName());
  }
  
  public void makeChangePasswordRequest(User user, String email, String firstName) {
    String token = UUID.randomUUID().toString();

    Token registrationToken = Token.builder()
        .tokenType(TokenType.CHANGE_PASSWORD)
        .token(token)
        .createdAt(LocalDateTime.now())
        .expiresAt(LocalDateTime.now().plusMinutes(activationTokenExpirationMinutes))
        .user(user)
        .build();

    tokenRepository.save(registrationToken);

    emailService.send(email, EmailService.getEmailHtmlTemplate(firstName, activateLink + token), "Set your password");
  }

  public AuthenticationResponse authenticate(AuthRequest authRequest) {
    authManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            authRequest.getEmail(),
            authRequest.getPassword()
        )
    );
    return new AuthenticationResponse(jwtService.generateToken(authRequest.getEmail()));
  }
}
