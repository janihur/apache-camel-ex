// https://camel.apache.org/components/latest/others/main.html
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

public class App {
    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.configure().addRoutesBuilder(
            new RouteBuilder() {
                public void configure() {
                    // triggers only once
                    from("timer:exampleTimer?repeatCount=1")
                    .routeId("TimerRoute")
                    .log("ROUTE START:")
                    // all headers and properties can be accessed in XSLT with
                    // <xsl:param name="EXTRA_LINE_1"/>
                    .setProperty("EXTRA_LINE_1", constant("Text for extra line 1 !"))
                    .setHeader("EXTRA_LINE_2", constant("Text for extra line 2 !"))
                    .setBody(method(Util.class, "getMsg1"))
                    .to("xslt:transformation.xsl")
                    .to("direct:logger")
                    .log("ROUTE END:")
                    ;

                    from("direct:logger")
                    .routeId("LoggerRoute")
                    .log("ROUTE START: body = ${body}")
                    .log("ROUTE END:")
                    ;
                }
            }
        );
        main.run(args);
    }
}
