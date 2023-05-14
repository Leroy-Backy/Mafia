package com.example.mafiaback.errorhandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MafiaErrorHandler {
  @ExceptionHandler
  public ResponseEntity<String> mafiaEntityNotFoundException(MafiaEntityNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }

  @ExceptionHandler
  public ResponseEntity<String> mafiaForbiddenException(MafiaForbiddenException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
  }

  @ExceptionHandler
  public ResponseEntity<String> accessDeniedException(AccessDeniedException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
  }

  @ExceptionHandler
  public ResponseEntity<String> exception(Exception e){
    return ResponseEntity.badRequest().body(e.getMessage());
  }
}
