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
                        .process(new SetInitialBodyValue()) // body will be a list
                        .log("MainRoute BEGINS: BODY: ${body}")
                        // check examples
                        .choice()
                            .when(bodyAs(List.class).isNotNull()) // or "${body} == null"
                                .log("BODY is not null")
                            .otherwise()
                                .log("BODY is null")
                        .end()
                        .choice()
                        .when(simple("${body} is 'java.util.List'"))
                            .log("BODY is an instance of List")
                        .otherwise()
                            .log("BODY is not an instance of list")
                        .end()
                        .choice()
                            .when(simple("${body.isEmpty()}"))
                                .log("list in BODY is empty")
                            .otherwise()
                                .log("list in BODY is not empty")
                        .end()
                        // split
                        .split(bodyAs(List.class))
                            .log("BODY: ${body}")
                            .choice()
                                .when(body().isInstanceOf(Foo.class))
                                    .log("BODY is an instance of Foo")
                                    .to("direct:foo")
                                .endChoice()
                                .when(simple("${body} is 'Bar'"))
                                    .log("BODY is an instance of Bar")
                                    .to("direct:bar")
                                .endChoice()
                            .end()
                        .end()
                        .log("MainRoute ENDS")
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
