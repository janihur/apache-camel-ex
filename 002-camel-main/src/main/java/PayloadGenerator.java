import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PayloadGenerator {
    public String generate() {
        return "{topic: \"lorem ipsum\", timestamp:\"" + getCurrentLocalDateTimeStamp() + "\"}";
    }
    private String getCurrentLocalDateTimeStamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }
}