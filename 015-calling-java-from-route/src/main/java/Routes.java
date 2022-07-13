import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.Exchange;

import java.util.ArrayList;
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
                        .process(e -> {
                            List<Foo> foos = new ArrayList<Foo>();
                            foos.add(new Foo(new FooRequest("1"), new FooResponseProcessor("1")));
                            foos.add(new Foo(new FooRequest("2"), new FooResponseProcessor("2")));
                            e.getMessage().setBody(foos, Foo.class);
                        })
                        .split(bodyAs(List.class))
                            .to("direct:secondRoute")
                        .end()
                        .log("MainRoute ENDS: BODY: ")
                    .end()
                    ;

                    from("direct:secondRoute")
                    .routeId("SecondRoute")
                        .log("SecondRoute BEGINS: BODY: ${body}")
                        .log("${body.request.request()}")
                        .process(e -> {
                            Foo foo = e.getMessage().getBody(Foo.class);
                            foo.responseProcessor.process(e);
                        })
                        .log("SecondRoute ENDS: BODY: ${body}")
                    .end()
                    ;
                }
            };
    }
}
