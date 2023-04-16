package com.example.mafiaback.errorhandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class MafiaEntityNotFoundException extends RuntimeException{
  public MafiaEntityNotFoundException(String message) {
    super(message);
  }
}
