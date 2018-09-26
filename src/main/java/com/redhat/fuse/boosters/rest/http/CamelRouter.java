package com.redhat.fuse.boosters.rest.http;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.springframework.stereotype.Component;

/**
 * A simple Camel REST DSL route that implements the greetings service.
 * 
 */
@Component
public class CamelRouter extends RouteBuilder {


    @Override
    public void configure() throws Exception {

        // @formatter:off
        restConfiguration()
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Greeting REST API")
                .apiProperty("api.version", "1.0")
                .apiProperty("cors", "true")
                .apiProperty("base.path", "camel/")
                .apiProperty("api.path", "/")
                .apiProperty("host", "")
//                .apiProperty("schemes", "")
                .apiContextRouteId("doc-api")
            .component("servlet")
            .bindingMode(RestBindingMode.json);
        
        rest("/greetings/").description("Greeting to {name}")
            .get("/{name}").outType(GreetingsGoodbye.class)
                .route().routeId("greeting-api")
            .multicast((AggregationStrategy) (exchange1, exchange2) -> {
                if (exchange1 == null) {
                    return exchange2;
                } else {
                    Greetings g1 = exchange1.getIn().getBody(Greetings.class);
                    Goodbye g2 = exchange2.getIn().getBody(Goodbye.class);
    
                    exchange1.getIn().setBody(new GreetingsGoodbye(g1, g2));
                    return exchange1;
                }
            })
            .parallelProcessing()
            .to("direct:greetingsImplRemote", "direct:goodbyeImpl");
    
        from("direct:greetingsImplRemote").description("Greetings REST service implementation route")
            .streamCaching()
            .to("http://www.mocky.io/v2/5bab67cd31000074006542ad?bridgeEndpoint=true&amp;throwExceptionOnFailure=false")
            .convertBodyTo(String.class)
            .unmarshal().json(JsonLibrary.Jackson, Greetings.class);
    
        from("direct:greetingsImplLocal").description("Greetings REST service implementation route")
            .streamCaching()
            .to("bean:greetingsService?method=getGreetings");
        
        from("direct:goodbyeImpl").description("Goodbye REST service implementation route")
            .streamCaching()
            .to("bean:goodbyeService?method=getGoodbye");
        // @formatter:on
    }

}