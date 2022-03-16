import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class SetInitialBodyValue implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        List<Object> objects = new ArrayList<Object>();
        objects.add(new Foo(1));
        objects.add(new Bar("A"));
        objects.add(new Foo(2));
        objects.add(new Bar("B"));
        exchange.getMessage().setBody(objects, List.class);
    }
}