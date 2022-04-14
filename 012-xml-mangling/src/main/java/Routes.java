import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.Exchange;
import org.w3c.dom.NodeList;

import java.util.List;

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
                        .setBody(constant("<_>\n" +
                            "  <foo>foo 1</foo>\n" +
                            "  <foo>foo 2</foo>\n" +
                            "  <foo>foo 3</foo>\n" +
                            "  <bar>bar</bar>\n" +
                            "</_>"))
                        .log("MainRoute BEGINS: BODY: ${body}")
                        // ----------------------------------------------------
                        .setProperty("FOOS1").xpath("//foo") // as NodeList
                        .setProperty("FOOS2").xpath("//foo/text()", List.class)
                        .setProperty("BAR").xpath("//bar", String.class)
                        .log("Properties:" +
                            "(FOOS1 \"${exchangeProperty.FOOS1}\")" +
                            "(FOOS2 \"${exchangeProperty.FOOS2}\")" +
                            "(BAR \"${exchangeProperty.BAR}\")"
                        )
                        // ----------------------------------------------------
                        .to("direct:splitFoos")
                        .to("direct:foosToXml")
                        .log("MainRoute ENDS: BODY: ")
                    .end()
                    ;

                    from("direct:splitFoos")
                    .routeId("SplitFoos")
                        .split(exchangeProperty("FOOS1"))
                            .log("FOOS1: ${body}")
                        .end()
                        .split(exchangeProperty("FOOS2"))
                            .log("FOOS2: ${body}")
                        .end()
                    .end()
                    ;

                    // XSLT parameters are NodeList and List of Strings
                    from("direct:foosToXml")
                    .routeId("FoosToXml")
                        .to("xslt-saxon:foos-to-xml.xsl")
                        .log("${body}")
                    .end()
                    ;
                }
            };
    }
}
