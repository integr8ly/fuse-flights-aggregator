package com.redhat.fuse.boosters.rest.http;

import com.google.common.collect.Lists;
import org.apache.camel.Header;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("arrivalsService")
public class ArrivalsServiceImpl implements ArrivalsService {

    @Override
    public List<Arrival> getArrivals() {
        return Lists.newArrayList(
            new Arrival("DUB", 1538050030000l),
            new Arrival("JFK", 1538051000000l),
            new Arrival("RDU", 1538052030000l),
            new Arrival("ATL", 1538053030000l)
        );
    }

}