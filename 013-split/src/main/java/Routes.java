import org.apache.camel.builder.RouteBuilder;

public class Routes {
    public static RouteBuilder routes1() {
        return
            new RouteBuilder() {
                public void configure() {
                    from("timer:mainRouteTrigger?repeatCount=1")
                    .routeId("MainRouteTrigger")
                        // all valid values
                        //.setBody(constant("100\n200\n300\n400"))
                        // invalid value (BOOM)
                        .setBody(constant("100\n200\n300\nBOOM\n400"))
                        .to("direct:mainRoute")
                    .end()
                    ;

                    from("direct:mainRoute")
                    .routeId("MainRoute")
                        .log("MainRoute BEGINS: BODY: ${body}")
                        .split(body().tokenize("\n"), new Aggregator())
                            .filter(method(ValueFilter.class, "isValidValue"))
                                .to("direct:singleRoute")
                            .end()
                        .end()
                        .log("MainRoute ENDS: BODY: ${body}")
                    .end()
                    ;

                    from("direct:singleRoute")
                    .routeId("SingleRoute")
                        .log("SingleRoute BEGINS: BODY: ${body}")
                        .process(e -> {
                            // this will throw when body can't be cast to Integer
                            Integer id = e.getMessage().getBody(Integer.class);
                            Integer calculatedValue = id + 1;
                            e.getMessage().setBody(String.format(
                                "\"calculated value for id %d is %d\"",
                                id,
                                calculatedValue
                            ));
                        })
                        .log("SingleRoute ENDS: BODY: ${body}")
                    .end()
                    ;
                }
            };
    }

    public static RouteBuilder routes2() {
        return
            new RouteBuilder() {
                public void configure() {
                    from("timer:mainRouteTrigger?repeatCount=1")
                        .routeId("MainRouteTrigger")
                        // all valid values
                        //.setBody(constant("100\n200\n300\n400"))
                        // invalid value (BOOM)
                        .setBody(constant("100\n200\n300\nBOOM\n400"))
                        .to("direct:mainRoute")
                    .end()
                    ;

                    from("direct:mainRoute")
                    .routeId("MainRoute")
                        .log("MainRoute BEGINS: BODY: ${body}")
                        .split(body().tokenize("\n"))
                            .multicast()
                                .to("direct:processRecord1")
                                .to("direct:processRecord2")
                            .end()
                        .end()
                        .log("MainRoute ENDS: BODY: ${body}")
                    .end()
                    ;

                    from("direct:processRecord1")
                    .routeId("processRecord1")
                        .filter(method(ValueFilter.class, "isValidValue"))
                        .log("processing record ${body}")
                    .end()
                    ;

                    from("direct:processRecord2")
                    .routeId("processRecord2")
                        .process(e -> {
                            String body = e.getMessage().getBody(String.class);
                            if (ValueFilter.isValidValue(body)) {
                                e.setProperty("PROCESSED", true);
                                e.getMessage().setBody("processing record " + body);
                            }
                        })
                        .choice()
                            .when(simple("${exchangeProperty.PROCESSED}"))
                                .log("${body}")
                    .end()
                    ;
                }
            };
    }
}
