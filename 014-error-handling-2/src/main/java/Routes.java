import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.Exchange;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class Routes {
    private static final int FAILURE_COUNT = 3;
    private static final int REDELIVERY_COUNT = 2;
    private static final long REDELIVERY_DELAY = TimeUnit.MILLISECONDS.convert(Duration.ofSeconds(4));

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
                        .onException(RuntimeException.class)
                            .maximumRedeliveries(Routes.REDELIVERY_COUNT)
                            .redeliveryDelay(Routes.REDELIVERY_DELAY)
                            // .handled(true) // the exception is not reported but this route is not continued
                            .continued(true) // this route is continued as the exception didn't happen
                            .to("direct:deadLetterChannel")
                        .end()
                        .setBody(constant("ORIGINAL MESSAGE"))
                        .log("MainRoute BEGINS: BODY: ${body}")
                        .to("direct:processStuff")
                        .log("MainRoute ENDS: BODY: ${body}")
                    .end()
                    ;

                    from("direct:processStuff")
                    .routeId("ProcessStuff")
                    .errorHandler(noErrorHandler())
                        .log("ProcessStuff BEGINS")
                        .process(new FailsFirst(Routes.FAILURE_COUNT))
                        .log("ProcessStuff ENDS")
                    .end()
                    ;

                    from("direct:deadLetterChannel")
                    .routeId("DeadLetterChannel")
                        .doTry()
                            // processing takes place here
                            // logging only
                            .log("DEAD LETTER CHANNEL: ${body}")
                            .process(e -> {
                                throw new RuntimeException("Problem in dead letter channel!");
                            })
                        .doCatch(Exception.class)
                            .log("CAUGHT AN EXCEPTION IN DEAD LETTER CHANNEL!")
                            .log("EXCEPTION: ${exception}")
                    .end()
                    ;
                }
            };
    }
}
