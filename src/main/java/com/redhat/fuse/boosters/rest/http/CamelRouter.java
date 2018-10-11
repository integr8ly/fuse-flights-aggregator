package com.redhat.fuse.boosters.rest.http;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
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
        String arrivalsHost = System.getenv("ARRIVALS_HOST");
        String departuresHost = System.getenv("DEPARTURES_HOST");
        
        if (arrivalsHost == null || arrivalsHost.isEmpty()) {
            throw new Exception("ARRIVALS_HOST env var not set");
        } else if(departuresHost == null || departuresHost.isEmpty()) {
            throw new Exception("DEPARTURES_HOST env var not set");
        }

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
                .apiProperty("schemes", "https")
                .apiContextRouteId("doc-api")
            .component("servlet")
            .bindingMode(RestBindingMode.json);
        
        rest("/flights")
            .description("List all flights (arrivals & departures)")
            .get()
            .param().name("user_key")
                .type(RestParamType.query)
                .required(false)
                .description("User Key, if calling the API in front of 3Scale.")
                .endParam()
            .outType(FlightsList.class)
            .route().routeId("flights-api")
            .multicast(new FlightAggregationStrategy())
            .parallelProcessing()

            //
            // COMMENT OUT THIS
            .to("direct:arrivalsImplLocal", "direct:departuresImplLocal");

            //
            // UNCOMMENT THIS
            //.to("direct:arrivalsImplRemote", "direct:departuresImplRemote");
    
        from("direct:arrivalsImplRemote").description("Arrivals REST service implementation route")
            .streamCaching()
            .to(String.format("http://%s/arrivals?bridgeEndpoint=true", arrivalsHost))
            .convertBodyTo(String.class)
            .unmarshal().json(JsonLibrary.Jackson, ArrivalsList.class);
    
        from("direct:departuresImplRemote").description("Departures REST service implementation route")
            .streamCaching()
            .to(String.format("http://%s/departures?bridgeEndpoint=true", departuresHost))
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