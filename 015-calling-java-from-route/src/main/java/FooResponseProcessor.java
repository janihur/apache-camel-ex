import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class FooResponseProcessor implements Processor {
    private String id;

    public FooResponseProcessor(String id) {
        this.id = id;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getMessage().setBody("Processed by FooResponseProcessor " + this.id, String.class);
    }
}