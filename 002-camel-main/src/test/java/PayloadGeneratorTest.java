import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class PayloadGeneratorTest {
    @Test
    @DisplayName("Valid payload format")
    void generate() {
        String regex = ".+topic:\\s+\"lorem ipsum\".+timestamp:\\s+.+";
        PayloadGenerator g = new PayloadGenerator();
        String got = g.generate();
        assertEquals(
            true,
            got.matches(regex)
        );
    }
}