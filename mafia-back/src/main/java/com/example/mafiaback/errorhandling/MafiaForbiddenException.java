package com.example.mafiaback.errorhandling;

public class MafiaForbiddenException extends RuntimeException {
  public MafiaForbiddenException(String message) {
    super(message);
  }
}
