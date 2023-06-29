import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.Exchange;

import java.util.concurrent.TimeUnit;

public class Routes {
    public static RouteBuilder routes() {
        return
            new RouteBuilder() {
                public void configure() {
                    from("timer:mainRouteTrigger?repeatCount=1")
                    .routeId("MainRouteTrigger")
                        .setBody(constant("ORIGINAL MESSAGE"))
                        .setProperty("P1", constant("ORGINAL P1 PROPERTY"))
                        .to("direct:mainRoute")
                    .end()
                    ;

                    from("direct:mainRoute")
                    .routeId("MainRoute")
                        .log("MainRoute BEGINS: (body \"${body}\")(P1 \"${exchangeProperty.P1}\")")
                        .wireTap("direct:firstSubRoute")
                        .wireTap("direct:secondSubRoute")
                        .setBody(constant("MAIN ROUTE MODIFIED"))
                        .log("MainRoute ENDS: BODY: ${body}")
                    .end()
                    ;

                    from("direct:firstSubRoute")
                    .routeId("FirstSubRoute")
                        .log("FirstSubRoute BEGINS: (body \"${body}\")(P1 \"${exchangeProperty.P1}\")")
                        .setBody(constant("FIRST SUB ROUTE MODIFIED"))
                        .process(e -> { TimeUnit.SECONDS.sleep(1);})
                        .log("FirstSubRoute ENDS: BODY: ${body}")
                    .end()
                    ;

                    from("direct:secondSubRoute")
                    .routeId("SecondSubRoute")
                        .log("SecondSubRoute BEGINS: (body \"${body}\")(P1 \"${exchangeProperty.P1}\")")
                        .process(e -> { TimeUnit.SECONDS.sleep(2);})
                        .log("SecondSubRoute BEGINS: (body \"${body}\")(P1 \"${exchangeProperty.P1}\")")
                        .log("SecondSubRoute ENDS: BODY: ${body}")
                        .end()
                    ;
                }
            };
    }
}
