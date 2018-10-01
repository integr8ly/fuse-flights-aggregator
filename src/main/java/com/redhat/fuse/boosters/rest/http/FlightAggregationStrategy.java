package com.redhat.fuse.boosters.rest.http;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import java.util.ArrayList;
import java.util.List;

public class FlightAggregationStrategy implements AggregationStrategy {
  
  @Override public Exchange aggregate(Exchange ex1, Exchange ex2) {
     if (ex1 == null) {
         return ex2;
     } else {
       List<Arrival> arrivals = ex1.getIn().getBody(ArrivalsList.class);
       List<Departure> departures = ex2.getIn().getBody(DeparturesList.class);

       List<Flight> flights = new ArrayList<>();
  
       for (Arrival arrival: arrivals) {
         flights.add(arrival);
       }
       for (Departure departure: departures) {
         flights.add(departure);
       }
     
       ex1.getIn().setBody(flights);
       return ex1;
     }
  }
}



