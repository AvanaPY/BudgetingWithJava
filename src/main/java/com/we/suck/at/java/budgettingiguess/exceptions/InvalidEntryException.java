package com.we.suck.at.java.budgettingiguess.exceptions;

import com.we.suck.at.java.budgettingiguess.models.TrackingEntry;

import java.util.Arrays;

public class InvalidEntryException extends RuntimeException {
  public InvalidEntryException(String message) {
    super(message);
  }
}
