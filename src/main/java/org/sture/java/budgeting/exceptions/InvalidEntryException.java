package org.sture.java.budgeting.exceptions;

public class InvalidEntryException extends RuntimeException {
  public InvalidEntryException(String message) {
    super(message);
  }
}
