package com.redhat.fuse.boosters.rest.http;

/**
 * Service interface for name service.
 * 
 */
public interface GoodbyeService {

    /**
     * Generate Goodbye
     *
     * @return a string goodbye
     */
    Goodbye getGoodbye(String name);

}