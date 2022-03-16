import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class Routes2Test extends CamelTestSupport {
    @Override
    protected RoutesBuilder[] createRouteBuilders() throws Exception {
        List<RoutesBuilder> routes = new ArrayList<RoutesBuilder>(3);

        routes.add(Routes.routes()); // the actual routes under testing

        routes.add(new RouteBuilder() { // test1 route
            @Override
            public void configure() throws Exception {
                from("direct:test1").routeId("Test1Route").log("TEST1").to("mock:test1");
            }
        });

        routes.add(new RouteBuilder() { // test2 routes
            @Override
            public void configure() throws Exception {
                from("direct:test2").routeId("Test2Route").log("TEST2").to("direct:mainRoute").to("mock:test2");
            }
        });

        return routes.toArray(new RoutesBuilder[0]);
    }

    @Test
    public void mockTest1() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:test1");
        mock.expectedMessageCount(2);
        mock.expectedBodiesReceived(List.of("TEST MESSAGE 1", "TEST MESSAGE 2"));

        template.sendBody("direct:test1", "TEST MESSAGE 1");
        template.sendBody("direct:test1", "TEST MESSAGE 2");

        mock.assertIsSatisfied();
    }

    @Test
    public void mockTest2() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:test2");
        mock.expectedMessageCount(1);
        mock.expectedBodiesReceived(List.of("String set to body in the beginning of MainRoute.\n" +
            "String appended to body in SubRoute1.\n" +
            "String appended to body in the middle of MainRoute.\n" +
            "String appended to body in SubRoute2."));

        template.sendBody("direct:test2", null);

        mock.assertIsSatisfied();
    }
}
