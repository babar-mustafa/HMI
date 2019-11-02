package com.hmi.assignment.enums;


public enum HttpRequestType {
  POST(0), PUT(1), GET(2), DELETE(3);

  private int value;

  HttpRequestType(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
