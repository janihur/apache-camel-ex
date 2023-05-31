import org.apache.camel.builder.RouteBuilder;

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
                        .to("direct:compare")
                        .to("direct:date")
                        .to("direct:emptyString")
                        .log("MainRoute ENDS: BODY: ")
                    .end()
                    ;

                    from("direct:compare")
                    .routeId("CompareRoute")
                        .log("CompareRoute BEGINS:")
                        // ----------------------------------------------------
                        // strings work as expected
                        .setProperty("A1").constant("FOO")
                        .setProperty("B1").constant("FOO")
                        .log("(A1 \"${exchangeProperty.A1}\")(B1 \"${exchangeProperty.B1}\")")
                        .choice()
                            .when(simple("${exchangeProperty.A1} == ${exchangeProperty.B1}"))
                                .log("A1 equals B1")
                            .otherwise()
                                .log("A1 doesn't equals B1")
                        .end()
                        .removeProperties("*")
                        // ----------------------------------------------------
                        // xpath doesn't return (a Java) string as default so this won't work:
                        // .setProperty("A2").xpath("/xml/a/text()")
                        // https://camel.apache.org/components/3.14.x/languages/xpath-language.html#_setting_result_type
                        .setBody(constant("<xml><a>FOO</a><b>FOO</b></xml>"))
                        .setProperty("A2").xpath("/xml/a", String.class) // now the property is a Java string
                        .setProperty("B2").xpath("/xml/b", String.class) // now the property is a Java string
                        .log("(A2 \"${exchangeProperty.A2}\")(B2 \"${exchangeProperty.B2}\")")
                        .choice()
                            // now comparing strings
                            .when(simple("${exchangeProperty.A2} == ${exchangeProperty.B2}"))
                                .log("A2 equals B2")
                            .otherwise()
                                .log("A2 doesn't equals B2")
                        .end()
                        .removeProperties("*")
                        // ----------------------------------------------------
                        .log("CompareRoute ENDS:")
                    .end()
                    ;

                    from("direct:date")
                    .routeId("DateRoute")
                        .log("DateRoute BEGINS:")
                        // date formats
                        // https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/text/SimpleDateFormat.html
                        .log("Date as string (local) NOW: ${date:now:yyyyMMdd}")
                        .log("Date as string (local) NOW: ${date:now:yyyy-MM-dd'T'HH:mm:ss}")
                        .log("Date as string (local) NOW: ${date:now:yyyy-MM-dd'T'HH:mm:ssXXX}")
                        // ---
                        .log("Date as string (UTC) NOW: ${date-with-timezone:now:UTC:yyyy-MM-dd'T'HH:mm:ssXXX}")
                        // ---
                        .log("Date as string (CET) NOW: ${date-with-timezone:now:CET:yyyy-MM-dd'T'HH:mm:ssX}")
                        .log("Date as string (UTC) NOW: ${date-with-timezone:now:UTC:yyyy-MM-dd'T'HH:mm:ssX}")
                        .log("Date as string (EST) NOW: ${date-with-timezone:now:EST:yyyy-MM-dd'T'HH:mm:ssX}")
                        // ---
                        .log("Date as string YESTERDAY: ${date:now-1d:yyyyMMdd}")
                        // ---
                        .log("Date as Date object: ${date:now}")
                        // ---
                        .log("DateRoute ENDS:")
                    .end()
                    ;

                    from("direct:emptyString")
                    .routeId("EmptyStringRoute")
                        .setBody(constant("<xml><a>FOO</a><b>BAR</b></xml>"))
                        .log("EmptyStringRoute BEGINS: ${body}")
                        .setProperty("A1").xpath("/xml/a", String.class) // now the property is a Java string
                        .setProperty("A2").xpath("/xml/x", String.class) // now the property is a Java string
                        .log("(A1 \"${exchangeProperty.A1}\")(A2 \"${exchangeProperty.A2}\")")
                        .choice()
                            .when(simple("${exchangeProperty.A1.isEmpty} == 'false'"))
                                .log("String A1 is NOT empty")
                            .otherwise()
                                .log("String A1 is empty")
                        .end()
                        .choice()
                            .when(simple("${exchangeProperty.A2.isEmpty}"))
                                .log("String A2 is empty")
                            .otherwise()
                                .log("String A2 is NOT empty")
                        .end()
                        .log("EmptyStringRoute ENDS:")
                    .end()
                    ;
                }
            };
    }
}
