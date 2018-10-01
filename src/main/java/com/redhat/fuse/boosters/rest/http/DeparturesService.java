package com.redhat.fuse.boosters.rest.http;

import java.util.List;

/**
 * Service interface for name service.
 * 
 */
public interface DeparturesService {

    /**
     * @return a list of departures
     */
    List<Departure> getDepartures();

}