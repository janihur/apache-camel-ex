import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Api2ResponseAggregator implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        // response -----------------------------------------------------------
        String response = newExchange.getMessage().getBody(String.class);
        System.err.println("API2_RESPONSE_AGGREGATOR: (response \"" + response + "\")");

        Object document = Configuration.defaultConfiguration().jsonProvider().parse(response);

        // API2_NEXT_PAGE property --------------------------------------------
        {
            String next = "null";
            Integer nextInt = JsonPath.read(document, "$.next"); // might be null
            if (Objects.nonNull(nextInt)) {
                next = nextInt.toString();
            }
            System.err.println("API2_RESPONSE_AGGREGATOR: (next \"" + next + "\")");

            oldExchange.setProperty("API2_NEXT_PAGE", next);
        }

        // API2_RESULTS property ----------------------------------------------
        {
            List<Map<String, Object>> newResults = JsonPath.read(document, "$.results");
            for (Map<String, Object> object : newResults) {
                System.err.println("API2_RESPONSE_AGGREGATOR: (object \"" + object.toString() + "\")");
            }

            @SuppressWarnings("unchecked") List<Map<String, Object>> oldResults =
                (List<Map<String, Object>>) oldExchange.getProperty("API2_RESULTS");

            if (Objects.isNull(oldResults)) {
                oldExchange.setProperty("API2_RESULTS", newResults);
            } else {
                List<Map<String, Object>> combined = new ArrayList<>();
                combined.addAll(oldResults);
                combined.addAll(newResults);
                oldExchange.setProperty("API2_RESULTS", combined);
            }
        }

        return oldExchange;
    }
}
