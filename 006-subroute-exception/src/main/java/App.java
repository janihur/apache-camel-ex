// https://camel.apache.org/components/latest/others/main.html
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

public class App {
    /**
     * Case #1: Exception policy set
     *
     * Exception in subroute is propagated to calling route (main route) when subroute
     * disables local error handler with .errorHandler(noErrorHandler()).
     *
     * If error handler is not disabled then exception is propagated to the exception policy.
     */
    private static RouteBuilder case1() {
        return
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
                .errorHandler(noErrorHandler())
                    .log("SUB ROUTE START: body = ${body}")
                    .process(payloadManipulator) // might throw
                    .log("SUB ROUTE END: body = ${body}")
                ;
            }
        };
    }

    /**
     * Case #2: Exception policy not set
     *
     * Exception in subroute is propagated to calling route (main route).
     */
    private static RouteBuilder case2() {
        return
        new RouteBuilder() {
            public void configure() {
                PayloadManipulatorProcessor payloadManipulator = new PayloadManipulatorProcessor();

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
        };
    }

    public static void main(String[] args) throws Exception {
        int case_ = 1;
        if (args.length > 0) {
            case_ = Integer.parseInt(args[0]);
        }

        Main main = new Main();

        switch (case_) {
            case 1:
                main.configure().addRoutesBuilder(case1());
                break;
            case 2:
                main.configure().addRoutesBuilder(case2());
                break;
            default:
                throw new Exception();
        }

        main.run(new String[]{"-duration", "5"});
    }
}
