package com.redhat.fuse.boosters.rest.http;

public class Arrival extends Flight {
  
  public Arrival(){}
  
  public Arrival(String code, Long time) {
    this.setCode(code);
    this.setTime(time);
  }
  
  @Override public String getFlightType() {
    return "arrival";
  }
}
