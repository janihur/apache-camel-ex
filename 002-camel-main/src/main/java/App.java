// https://camel.apache.org/components/latest/others/main.html
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

public class App {
    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.configure().addRoutesBuilder(
            new RouteBuilder() {
                public void configure() {
                    from("direct:savefile")
                    .routeId("SaveFileRoute")
                    .log("ROUTE START: body = ${body}")
                    .to("file://outbox")
                    .log("ROUTE END:")
                    ;
                }
            }
        );
        main.configure().addRoutesBuilder(
            new RouteBuilder() {
                public void configure() {
                    // triggers on every 8 seconds after initial 5 seconds delay
                    // https://camel.apache.org/components/latest/quartz-component.html
                    from("quartz://exampleTimer?startDelayedSeconds=5&cron=0/8+*+*+*+*+?")
                    .routeId("TimerRoute")
                    .log("ROUTE START:")
                    // https://camel.apache.org/components/latest/languages/bean-language.html
                    .setBody(method(PayloadGenerator.class, "generate"))
                    // https://camel.apache.org/components/latest/eips/setBody-eip.html
                    // .setBody(constant("HELLO FROM TIMER!"))
                    .to("direct:savefile")
                    .log("ROUTE END:")
                    ;
                }
            }
        );
        main.run(args);
    }
}
