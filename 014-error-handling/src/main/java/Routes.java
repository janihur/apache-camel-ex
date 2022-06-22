import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.Exchange;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class Routes {
    public static RouteBuilder routes() {
        return
            new RouteBuilder() {
                public void configure() {
                    from("timer:mainRouteTrigger?repeatCount=1")
                    .routeId("MainRouteTrigger")
                        .to("direct:mainRoute")
                    .end()
                    ;

                    from("direct:mainRoute")
                    .routeId("MainRoute")
                        .doTry()
                            .log("MainRoute BEGINS:")
                            // .to("direct:failsAlways")
                            .to("direct:failsFirst1")
                            .log("MainRoute ENDS:")
                        .endDoTry()
                        .doCatch(Exception.class)
                            .log("MainRoute EXCEPTION:")
                    .end()
                    ;

                    from("direct:failsAlways")
                    .routeId("FailsAlways")
                        .onException(java.lang.RuntimeException.class)
                            .maximumRedeliveries(4)
                            .redeliveryDelay(TimeUnit.MILLISECONDS.convert(Duration.ofSeconds(5)))
                        .end()
                        .log("FailsAlways BEGINS:")
                        .process(new FailsAlways())
                        .log("FailsAlways ENDS:")
                    .end()
                    ;

                    from("direct:failsFirst1")
                    .routeId("FailsFirst1")
                        .onException(java.lang.RuntimeException.class)
                            .maximumRedeliveries(4)
                            .redeliveryDelay(TimeUnit.MILLISECONDS.convert(Duration.ofSeconds(2)))
                            .backOffMultiplier(2)
                        .end()
                        .log("FailsFirst1 BEGINS:")
                        .to("direct:processStuff1")
                        .log("FailsFirst1 ENDS:")
                    .end()
                    ;

                    from("direct:processStuff1")
                    .routeId("ProcessStuff1")
                        .doTry()
                            .process(new FailsFirst(3))
                        .doCatch(RuntimeException.class)
                            .log("CAUGHT AN EXCEPTION! Do what's needed at this point and then going to rethrow for redelivery!")
                            .log("EXCEPTION: ${exception}")
                            .process(e -> { // rethrow
                                throw e.getException();
                            })
                    .end()
                    ;
                }
            };
    }
}
