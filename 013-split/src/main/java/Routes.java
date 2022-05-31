import org.apache.camel.builder.RouteBuilder;

public class Routes {
    public static RouteBuilder routes() {
        return
            new RouteBuilder() {
                public void configure() {
                    from("timer:mainRouteTrigger?repeatCount=1")
                    .routeId("MainRouteTrigger")
                        .setBody(constant("100\n200\n300"))
                        .to("direct:mainRoute")
                    .end()
                    ;

                    from("direct:mainRoute")
                    .routeId("MainRoute")
                        .log("MainRoute BEGINS: BODY: ${body}")
                        .split(body().tokenize("\n"), new Aggregator())
                            .to("direct:singleRoute")
                        .end()
                        .log("MainRoute ENDS: BODY: ${body}")
                    .end()
                    ;

                    from("direct:singleRoute")
                    .routeId("SingleRoute")
                        .log("SingleRoute BEGINS: BODY: ${body}")
                        .setBody(simple("this is a response for id ${body}"))
                        .log("SingleRoute ENDS: BODY: ${body}")
                    .end()
                    ;
                }
            };
    }
}
