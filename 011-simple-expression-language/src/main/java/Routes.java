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
                        .to("direct:date")
                        .log("MainRoute ENDS: BODY: ")
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
                }
            };
    }
}
