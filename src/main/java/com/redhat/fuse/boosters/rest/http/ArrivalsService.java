package com.redhat.fuse.boosters.rest.http;

import java.util.List;

/**
 * Service interface for name service.
 * 
 */
public interface ArrivalsService {

    /**
     * @return a list of arrivals
     */
    List<Arrival> getArrivals();

}