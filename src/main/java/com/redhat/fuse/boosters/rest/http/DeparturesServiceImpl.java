package com.redhat.fuse.boosters.rest.http;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("departuresService")
public class DeparturesServiceImpl implements DeparturesService {

    @Override
    public List<Departure> getDepartures() {
        return Lists.newArrayList(
            new Departure("CDG", 1538054430000l),
            new Departure("JFK", 1538055500000l),
            new Departure("EWR", 1538056630000l),
            new Departure("MIA", 1538057730000l)
        );
    }
}