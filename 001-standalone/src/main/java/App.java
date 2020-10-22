// https://camel.apache.org/manual/latest/faq/running-camel-standalone.html
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class App {
    public static void main(String[] args) {
        CamelContext context = new DefaultCamelContext();
        context.setStreamCaching(true);
        
        // log - https://camel.apache.org/components/latest/eips/log-eip.html
        try {
            context.addRoutes(new RouteBuilder() {
                public void configure() {
                    from("direct:example")
                    .routeId("ExampleRoute")
                    .log("HELLO!")
                    .to("file://outbox")
                    ;
               }
            });
        } catch (Exception e) {
            System.err.println("EXCEPTION RouteBuilder:");
            System.err.println(e);
        }

        ProducerTemplate template = context.createProducerTemplate();
        context.start();
        template.sendBody("direct:example", "{topic: \"lorem ipsum\", timestamp:\"" + getCurrentLocalDateTimeStamp() + "\"}");
        
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            System.err.println("EXCEPTION Thread.sleep:");
            System.err.println(e);
        }

        context.stop();
    }

    // https://stackoverflow.com/a/20677345/272735
    private static String getCurrentLocalDateTimeStamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }
}
