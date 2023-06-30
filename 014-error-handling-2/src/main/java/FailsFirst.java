import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.time.LocalDateTime;

/**
 * Fails first `failures` calls.
 */
public class FailsFirst implements Processor {
    private int failuresLeft;
    public FailsFirst(int failures) {
        this.failuresLeft = failures;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        System.err.println("FailsFirst processor BEGINS: " + LocalDateTime.now());
        if (this.failuresLeft > 0) {
            this.failuresLeft--;
            Error error = new Error("FailsFirst processor ENDS: ERROR failures left: " + this.failuresLeft);
            System.err.println("FailsFirst processor ENDS: FAILURE: " + error.toString());
            throw error;
        }
        exchange.getMessage().setBody("MODIFIED MESSAGE");
        System.err.println("FailsFirst processor ENDS: SUCCESS");
    }
}