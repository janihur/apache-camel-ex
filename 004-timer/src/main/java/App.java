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
                    // just logging here
                    // .to("file://outbox")
                    .log("ROUTE END:")
                    ;
                }
            }
        );
        main.configure().addRoutesBuilder(
            new RouteBuilder() {
                public void configure() {
                    // triggers on every 3 seconds
                    // https://camel.apache.org/components/latest/timer-component.html
                    from("timer:exampleTimer?fixedRate=true&period=3s&repeatCount=5")
                    .routeId("TimerRoute")
                    .log("ROUTE START:")
                    // https://camel.apache.org/components/latest/eips/setBody-eip.html
                    .setBody()
                    // https://camel.apache.org/components/latest/languages/bean-language.html
                    .method(Foo.class, "foo")
                    .to("direct:savefile")
                    .log("ROUTE END:")
                    ;
                }
            }
        );
        main.run(new String[]{"-duration", "15"});
    }
}
