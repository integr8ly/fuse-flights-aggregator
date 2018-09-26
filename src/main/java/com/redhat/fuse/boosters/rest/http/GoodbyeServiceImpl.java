package com.redhat.fuse.boosters.rest.http;

import org.apache.camel.Header;
import org.springframework.stereotype.Service;

@Service("goodbyeService")
public class GoodbyeServiceImpl implements GoodbyeService {

    private static final String THE_GOODBYE = "Goodbye, ";

    @Override
    public Goodbye getGoodbye(@Header("name") String name ) {
        return new Goodbye( THE_GOODBYE + name );
    }

}