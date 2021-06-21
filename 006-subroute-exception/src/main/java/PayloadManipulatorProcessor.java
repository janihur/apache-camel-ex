import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class PayloadManipulatorProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        counter = counter + 1;
        String payload = counterToString(counter);
        exchange.getMessage().setBody(payload);
        switch (counter) {
            case 1:
            case 3:
                System.err.println("THE PROCESSOR IS GOING TO RAISE AN EXCEPTION: " + payload);
                throw new Exception("Too long payload !");
            default:
                System.err.println("THE PROCESSOR IS OK: " + payload);
        }
    }

    public PayloadManipulatorProcessor() {
        counter = 0;
    }

    private int counter;

    private String counterToString(int counter) {
        switch (counter) {
            case 0:
                return "";
            case 1:
                return "A";
            case 2:
                return "AA";
            case 3:
                return "AAA";
            default:
                return "AAAA";
        }
    }
}
