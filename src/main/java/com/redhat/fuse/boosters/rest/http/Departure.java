package com.redhat.fuse.boosters.rest.http;

public class Departure extends Flight {
  
  public Departure() {}
  
  public Departure(String code, Long time) {
    this.setCode(code);
    this.setTime(time);
  }

  @Override public String getFlightType() {
    return "departure";
  }
}
