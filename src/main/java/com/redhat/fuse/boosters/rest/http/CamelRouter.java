package com.redhat.fuse.boosters.rest.http;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * A simple Camel REST DSL route that implements the arrivals service.
 * 
 */
@Component
public class CamelRouter extends RouteBuilder {


    @Override
    public void configure() throws Exception {

        // @formatter:off
        restConfiguration()
            .enableCORS(true)
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Airport Flights REST API")
                .apiProperty("api.version", "1.0")
                .apiProperty("cors", "true")
                .apiProperty("base.path", "camel/")
                .apiProperty("api.path", "/")
                .apiProperty("host", "")
//                .apiProperty("schemes", "")
                .apiContextRouteId("doc-api")
            .component("servlet")
            .bindingMode(RestBindingMode.json);
        
        rest("/flights")
            .description("List all flights (arrivals & departures)")
            .get()
            .outType(FlightsList.class)
            .route().routeId("flights-api")
            .multicast(new FlightAggregationStrategy())
            .parallelProcessing()
            // 
            // NOTE: To switch between local & remote services:
            //    -  comment out the line that routes to local services
            //    -  uncomment the line that routes to remote services
            // 
            .to("direct:arrivalsImplLocal", "direct:departuresImplLocal");
            // .to("direct:arrivalsImplRemote", "direct:departuresImplRemote");
    
        from("direct:arrivalsImplRemote").description("Arrivals REST service implementation route")
            .streamCaching()
            .to("http://arrivals-server/arrivals")
            .convertBodyTo(String.class)
            .unmarshal().json(JsonLibrary.Jackson, ArrivalsList.class);
    
        from("direct:departuresImplRemote").description("Departures REST service implementation route")
            .streamCaching()
            .to("http://departures-server/departures")
            .convertBodyTo(String.class)
            .unmarshal().json(JsonLibrary.Jackson, DeparturesList.class);
    
        from("direct:arrivalsImplLocal").description("Arrivals REST service implementation route")
            .streamCaching()
            .to("bean:arrivalsService?method=getArrivals");
        
        from("direct:departuresImplLocal").description("Departures REST service implementation route")
            .streamCaching()
            .to("bean:departuresService?method=getDepartures");
        // @formatter:on
    }

}