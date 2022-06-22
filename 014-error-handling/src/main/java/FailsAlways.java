import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.time.LocalDateTime;

/**
 *
 */
public class FailsAlways implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        System.err.println("FailsAlways processor BEGINS: " + LocalDateTime.now());
        throw new RuntimeException("FailsAlways processor ENDS: ERROR");
    }
}