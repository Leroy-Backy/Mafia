package com.example.mafiaback.errorhandling;

import org.springframework.security.core.AuthenticationException;

public class MafiaUnauthorizedException extends AuthenticationException {
  public MafiaUnauthorizedException(String message) {
    super(message);
  }
}
