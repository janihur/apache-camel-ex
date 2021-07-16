import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.reifier.RouteReifier;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.util.List;

public class Routes3Test extends CamelTestSupport {
    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return Routes.routes();
    }

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @Test
    public void adviseWith_test1() throws Exception {
        ModelCamelContext mcc = context().adapt(ModelCamelContext.class);
        RouteReifier.adviceWith(mcc.getRouteDefinition("MainRoute"), mcc, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                replaceFromWith("direct:testStartRoute");

                weaveById("MainRouteSubRoute1").before().setBody(constant("You have been MOCKED !"));

                weaveById("MainRouteSubRoute1").replace().process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        String b = exchange.getMessage().getBody(String.class);
                        b = b + "\nRandom string injected by EVIL processor !";
                        exchange.getMessage().setBody(b, String.class);
                    }
                });

                weaveById("MainRouteSubRoute2").replace()
                    .log("Mock BEGINS: BODY: ${body}")
                    .transform(bodyAs(String.class).append("\nString appended to body in MOCK MOCK MOCK !"))
                    .log("Mock ENDS: BODY: ${body}")
                ;

                weaveAddLast().to("mock:test1");
            }
        });

        context.start();

        MockEndpoint mock = getMockEndpoint("mock:test1");
        mock.expectedMessageCount(1);
        mock.expectedBodiesReceived(List.of("You have been MOCKED !\n" +
            "Random string injected by EVIL processor !\n" +
            "String appended to body in the middle of MainRoute.\n" +
            "String appended to body in MOCK MOCK MOCK !"));

        template.sendBody("direct:testStartRoute", null);

        mock.assertIsSatisfied();

        context.stop();
    }

    @Test
    public void adviseWith_test2() throws Exception {
        RouteReifier.adviceWith(context.getRouteDefinition("MainRoute"), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveById("MainRouteSubRoute1").remove();
                weaveById("MainRouteSubRoute2").remove();
                weaveAddLast().to("mock:test2");
            }
        });

        context.start();

        MockEndpoint mock = getMockEndpoint("mock:test2");
        mock.expectedMessageCount(1);
        mock.expectedBodiesReceived(List.of("String set to body in the beginning of MainRoute.\n" +
            "String appended to body in the middle of MainRoute."));

        template.sendBody("direct:mainRoute", null);

        mock.assertIsSatisfied();

        context.stop();
    }

    @Test
    public void adviseWith_test3() throws Exception {
        AdviceWithRouteBuilder.adviceWith(context(), "MainRoute", a -> {
            a.weaveById("MainRouteSubRoute1").remove();
            a.weaveById("MainRouteSubRoute2").remove();
            a.weaveAddLast().to("mock:test3");
        });

        context.start();

        MockEndpoint mock = getMockEndpoint("mock:test3");
        mock.expectedMessageCount(1);
        mock.expectedBodiesReceived(List.of("String set to body in the beginning of MainRoute.\n" +
            "String appended to body in the middle of MainRoute."));

        template.sendBody("direct:mainRoute", null);

        mock.assertIsSatisfied();

        context.stop();
    }
}
