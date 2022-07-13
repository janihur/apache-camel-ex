import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Aggregator implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        // first iteration
        // oldExchange is null
        // body of newExchange is String
        if (oldExchange == null) {
            List<String> newList = buildListFrom(newExchange);
            newExchange.getMessage().setBody(newList, List.class);
            return newExchange;
        }

        // second and subsequent iterations
        // body of oldExchange is List<String>
        // body of newExchange is String
        @SuppressWarnings("unchecked") List<String> oldList = oldExchange.getMessage().getBody(List.class);
        List<String> newList = buildListFrom(newExchange);

        List<String> combined = Stream.concat(oldList.stream(), newList.stream()).collect(Collectors.toList());
        oldExchange.getMessage().setBody(combined);

        return oldExchange;
    }

    private List<String> buildListFrom(Exchange exchange) {
        String body = exchange.getMessage().getBody(String.class);
        List<String> list = new ArrayList<String>();
        list.add(body);
        return list;
    }
}
