package com.redhat.fuse.boosters.rest.http;

public abstract class Flight {
  
  private String code;
  private Long time;
  
  public String getCode() {
    return code;
  }
  
  public void setCode(String code) {
    this.code = code;
  }
  
  public Long getTime() {
    return time;
  }
  
  public void setTime(Long time) {
    this.time = time;
  }
  
  public abstract String getFlightType();
}
