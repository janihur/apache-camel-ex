// https://camel.apache.org/components/latest/others/main.html
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

public class App {
    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.configure().addRoutesBuilder(
            new RouteBuilder() {
                public void configure() {
                    PayloadManipulatorProcessor payloadManipulator = new PayloadManipulatorProcessor();
                    
                    onException(Exception.class)
                        .handled(true)
                        .log("EXCEPTION POLICY: body = ${body}")
                    ;

                    from("timer:exampleTimer?repeatCount=3")
                    .routeId("MainRoute")
                        .log("MAIN ROUTE START: body = ${body}")
                        .doTry()
                            .process(payloadManipulator) // might throw
                            .to("direct:subroute") // subroute might throw
                        .endDoTry()
                        .doCatch(Exception.class)
                            .log("MAIN ROUTE CATCH EXCEPTION: body = ${body}")
                        .end()
                        .log("MAIN ROUTE END: body = ${body}")
                    ;

                    from("direct:subroute")
                    .routeId("SubRoute")
                        .log("SUB ROUTE START: body = ${body}")
                        .process(payloadManipulator) // might throw
                        .log("SUB ROUTE END: body = ${body}")
                    ;
                }
            }
        );
        main.run(args);
    }
}
