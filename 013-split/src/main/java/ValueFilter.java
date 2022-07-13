import org.apache.camel.Body;

public class ValueFilter {
    static public boolean isValidValue(@Body String body) {
        switch (body) {
            case "100":
            case "200":
            case "300":
            case "400":
                return true;
            default:
                return false;
        }
    }
}
