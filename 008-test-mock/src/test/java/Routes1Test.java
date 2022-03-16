import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Routes1Test extends CamelTestSupport {
    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:test1").routeId("Test1Route").log("BING BING BING!").to("mock:test1");
            }
        };
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
}
