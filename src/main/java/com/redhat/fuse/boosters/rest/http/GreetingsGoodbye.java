package com.redhat.fuse.boosters.rest.http;

/**
 * Greetings entity
 *
 */
public class GreetingsGoodbye {

    private String greetings;
    private String goodbye;

    public GreetingsGoodbye() {
    }

    public GreetingsGoodbye(Greetings greetings, Goodbye goodbye) {
        this.greetings = greetings.getGreetings();
        this.goodbye = goodbye.getGoodbye();
    }
    
    public String getGoodbye() {
        return goodbye;
    }
    
    public void setGoodbye(String goodbye) {
        this.goodbye = goodbye;
    }
    
    public String getGreetings() {
        return greetings;
    }
    
    public void setGreetings(String greetings) {
        this.greetings = greetings;
    }
    
}