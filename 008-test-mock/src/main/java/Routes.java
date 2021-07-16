import org.apache.camel.builder.RouteBuilder;

public class Routes {
    /**
     * Simple routes that can be used to demonstrate adviseWith Camel feature.
     * All parts that should be mocked in the testing should be dedicated routes.
     */
    public static RouteBuilder routes() {
        return
            new RouteBuilder() {
                public void configure() {
                    from("timer:mainRouteTrigger?repeatCount=1")
                    .routeId("MainRouteTrigger")
                        .to("direct:mainRoute")
                    .end()
                    ;

                    // the orchestration route
                    from("direct:mainRoute")
                    .routeId("MainRoute")
                        .setBody(simple("String set to body in the beginning of MainRoute."))
                        .log("MainRoute BEGINS: BODY: ${body}")
                        // demonstrates a call to external system
                        .to("direct:subRoute1").id("MainRouteSubRoute1") // id required for mock
                        .transform(bodyAs(String.class).append("\nString appended to body in the middle of MainRoute."))
                        // demonstrates a call to another external system
                        .to("direct:subRoute2").id("MainRouteSubRoute2") // id required for mock
                        .log("MainRoute ENDS: BODY: ${body}")
                    .end()
                    ;

                    from("direct:subRoute1")
                    .routeId("SubRoute1")
                        .log("SubRoute1 BEGINS: BODY: ${body}")
                        .transform(bodyAs(String.class).append("\nString appended to body in SubRoute1."))
                        .log("SubRoute1 ENDS: BODY: ${body}")
                    .end()
                    ;

                    from("direct:subRoute2")
                    .routeId("SubRoute2")
                        .log("SubRoute2 BEGINS: BODY: ${body}")
                        .transform(bodyAs(String.class).append("\nString appended to body in SubRoute2."))
                        .log("SubRoute2 ENDS: BODY: ${body}")
                    .end()
                    ;
                }
            };
    }

}
