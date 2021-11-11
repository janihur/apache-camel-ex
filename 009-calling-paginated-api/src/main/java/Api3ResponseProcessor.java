import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 */
public class Api3ResponseProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        // response -----------------------------------------------------------
        String response = exchange.getMessage().getBody(String.class);
        System.err.println("API3_RESPONSE_AGGREGATOR: (response \"" + response + "\")");

        Object document = Configuration.defaultConfiguration().jsonProvider().parse(response);

        // API3_NEXT_PAGE property --------------------------------------------
        {
            String next = "null";
            Integer nextInt = JsonPath.read(document, "$.next"); // might be null
            if (Objects.nonNull(nextInt)) {
                next = nextInt.toString();
            }
            System.err.println("API3_RESPONSE_AGGREGATOR: (next \"" + next + "\")");

            exchange.setProperty("API3_NEXT_PAGE", next);
        }

        // API3_RESULTS property ----------------------------------------------
        {
            List<Map<String, Object>> newResults = JsonPath.read(document, "$.results");
            for (Map<String, Object> object : newResults) {
                System.err.println("API3_RESPONSE_AGGREGATOR: (object \"" + object.toString() + "\")");
            }

            @SuppressWarnings("unchecked") List<Map<String, Object>> oldResults =
                (List<Map<String, Object>>) exchange.getProperty("API3_RESULTS");

            if (Objects.isNull(oldResults)) {
                exchange.setProperty("API3_RESULTS", newResults);
            } else {
                List<Map<String, Object>> combined = new ArrayList<>();
                combined.addAll(oldResults);
                combined.addAll(newResults);
                exchange.setProperty("API3_RESULTS", combined);
            }
        }

        exchange.getMessage().setBody(null, String.class);
        exchange.getMessage().removeHeaders("Camel*");

    }
}