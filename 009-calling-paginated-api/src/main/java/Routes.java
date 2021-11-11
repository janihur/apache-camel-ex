import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.Exchange;

public class Routes {
    public static RouteBuilder routes() {
        return
            new RouteBuilder() {
                Api1ResponseAggregator api1Aggregator = new Api1ResponseAggregator();
                Api2ResponseAggregator api2Aggregator = new Api2ResponseAggregator();
                Api3ResponseProcessor  api3Response   = new Api3ResponseProcessor();

                public void configure() {
                    from("timer:mainRouteTrigger?repeatCount=1")
                    .routeId("MainRouteTrigger")
                        .to("direct:mainRoute")
                    .end()
                    ;

                    from("direct:mainRoute")
                    .routeId("MainRoute")
                        .log("MainRoute BEGINS: BODY: ${body}")
                        // using loop -----------------------------------------
                        .to("direct:callApi1")
                        .log("(API1_RESULTS \"${exchangeProperty.API1_RESULTS}\")")
                        // using recursion ------------------------------------
                        .setProperty("API2_NEXT_PAGE", simple("1", String.class)) // stop condition initial value
                        .to("direct:callApi2")
                        .log("(API2_RESULTS \"${exchangeProperty.API2_RESULTS}\")")
                        // using recursion 2 ----------------------------------
                        .setProperty("API3_NEXT_PAGE", simple("1", String.class)) // stop condition initial value
                        .to("direct:callApi3")
                        .log("(API3_RESULTS \"${exchangeProperty.API3_RESULTS}\")")
                        // ----------------------------------------------------
                        .log("MainRoute ENDS: BODY: ${body}")
                    .end()
                    ;

                    // loop ---------------------------------------------------
                    from("direct:callApi1")
                    .routeId("CallApi1")
                        .setProperty("API1_NEXT_PAGE", simple("1", String.class))
                        .loopDoWhile(simple("${exchangeProperty.API1_NEXT_PAGE} != 'null'"))
                            .log("(API1_NEXT_PAGE \"${exchangeProperty.API1_NEXT_PAGE}\")")
                            .setHeader(Exchange.HTTP_PATH, simple("/page/${exchangeProperty.API1_NEXT_PAGE}"))
                            // enrich + AggregationStrategy is most likely unnecessary
                            .enrich("http:localhost:8080", api1Aggregator)
                        .end()
                        .removeProperty("API1_NEXT_PAGE")
                    .end()
                    ;

                    // recursion ----------------------------------------------
                    from("direct:callApi2")
                    .routeId("CallApi2")
                        .choice().when(simple("${exchangeProperty.API2_NEXT_PAGE} != 'null'"))
                            .log("(API2_NEXT_PAGE \"${exchangeProperty.API2_NEXT_PAGE}\")")
                            .setHeader(Exchange.HTTP_PATH, simple("/page/${exchangeProperty.API2_NEXT_PAGE}"))
                            // enrich + AggregationStrategy is most likely unnecessary
                            .enrich("http:localhost:8080", api2Aggregator)
                            .to("direct:callApi2")
                        .endChoice()
                        .otherwise()
                            .removeProperty("API2_NEXT_PAGE")
                        .end()
                    .end()
                    ;

                    // recursion 2 --------------------------------------------
                    from("direct:callApi3")
                    .routeId("CallApi3")
                        .choice().when(simple("${exchangeProperty.API3_NEXT_PAGE} != 'null'"))
                            .log("(API3_NEXT_PAGE \"${exchangeProperty.API3_NEXT_PAGE}\")")
                            .setHeader(Exchange.HTTP_PATH, simple("/page/${exchangeProperty.API3_NEXT_PAGE}"))
                            .to("http:localhost:8080")
                            .process(api3Response)
                            .to("direct:callApi3")
                        .endChoice()
                        .otherwise()
                            .removeProperty("API3_NEXT_PAGE")
                        .end()
                    .end()
                    ;
                }
            };
    }
}
