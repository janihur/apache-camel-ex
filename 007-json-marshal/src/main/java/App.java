// https://camel.apache.org/components/latest/others/main.html
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.main.Main;
import org.apache.camel.model.dataformat.JsonLibrary;

public class App {
    /**
     * Simple JSON unmarshal/marshal example with Jackson:
     *
     * 1. convert JSON to Java objects (unmarshal)
     * 2. modify the content of the Java objects (processor)
     * 3. convert the modified Java objects to JSON (marshal)
     */
    private static RouteBuilder case1() {
        return
        new RouteBuilder() {
            public void configure() {
                from("timer:mainRoute?repeatCount=1")
                .routeId("MainRoute")
                    .setBody(simple("{\n" +
                        "\"description\": \"A harmless list of fruits.\",\n" +
                        "  \"fruits\": [\n" +
                        "    {\n" +
                        "      \"id\": 101,\n" +
                        "      \"name\": \"apple\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"id\": 102,\n" +
                        "      \"name\": \"banana\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"id\": 103,\n" +
                        "      \"name\": \"cherry\"\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}"))
                    .unmarshal(new JacksonDataFormat(RootObject.class)) // to Java objects
                    .log("body is now an object: ${body}")
                    .log("original RootObject.description: ${body.description}") // RootObject.getDescription()
                    .log("fruits[0].name: ${body.fruits[0].name}") // RootObject.getFruits().get(0).getName()
                    .process(exchange -> { // modify Java objects
                        RootObject ro = exchange.getMessage().getBody(RootObject.class);
                        ro.getFruits().get(0).setName("Apple™ Copyright © 2021 Apple Inc. All rights reserved.");
                        ro.setDescription(ro.getDescription() + " Corrected for copyright violations.");
                        exchange.getMessage().setBody(ro, RootObject.class);
                    })
                    .log("modified RootObject.description: ${body.description}") // RootObject.getDescription()
                    .marshal().json(JsonLibrary.Jackson, RootObject.class, true) // to JSON
                    .log("modified body = ${body}")
                    .end()
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
            default:
                throw new Exception();
        }

        main.run(new String[]{"-duration", "5"});
    }
}
