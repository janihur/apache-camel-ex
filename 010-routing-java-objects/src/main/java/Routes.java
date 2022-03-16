import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.Exchange;

import java.util.List;

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
                        .log("MainRoute BEGINS: BODY: ")
                        .process(new SetInitialBodyValue())
                        .split(bodyAs(List.class))
                            .log("BODY: ${body}")
                            .choice()
                                .when(body().isInstanceOf(Foo.class))
                                    .log("BODY is instance of Foo")
                                    .to("direct:foo")
                                .endChoice()
                                .when(simple("${body} is 'Bar'"))
                                    .log("BODY is instance of Bar")
                                    .to("direct:bar")
                                .endChoice()
                            .end()
                        .end()
                        .log("MainRoute ENDS: BODY: ")
                    .end()
                    ;

                    from("direct:foo")
                    .routeId("FooRoute")
                        .log("Foo value: ${body.fooId}")
                    .end()
                    ;

                    from("direct:bar")
                    .routeId("BarRoute")
                        .log("Bar value: ${body.barId}")
                    .end()
                    ;
                }
            };
    }
}
