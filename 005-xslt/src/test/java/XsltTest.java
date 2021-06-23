import java.io.InputStream;

import javax.xml.transform.Source;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.xmlunit.assertj3.XmlAssert.assertThat;

import org.xmlunit.builder.Input;

public class XsltTest {
    @Test
    @DisplayName("Simplified XMLUnit With A Little Bit More Complex XSLT Transformation With Parameters")
    void test_example_3() {
        Source expected = 
            Input.fromStream(resource("test-002-expected.xml"))
            .build()
            ;
        // simplified xslt transformations
        // https://github.com/xmlunit/user-guide/wiki/XSLT-Support
        Source got = 
            Input.byTransforming(Input.fromStream(resource("test-002-input.xml")))
            .withParameter("API-Client-Username", "JANI")
            .withParameter("ENV", "TEST1")
            .withStylesheet(Input.fromStream(resource("request.xsl")))
            .build()
            ;
        assertThat(got)
            .and(expected)
            .normalizeWhitespace()
            .ignoreComments()
            .areSimilar()
            ;
    }
    @Test
    @DisplayName("Simplified XMLUnit XSLT Transformation With Parameters")
    void test_example_2() {
        Source expected = 
            Input.fromStream(resource("test-001-expected-2.xml"))
            .build()
            ;
        // simplified xslt transformations
        // https://github.com/xmlunit/user-guide/wiki/XSLT-Support
        Source got = 
            Input.byTransforming(Input.fromStream(resource("test-001-input.xml")))
            .withParameter("EXTRA_LINE_1", "This line comes from PARAMETER 1 !")
            .withParameter("EXTRA_LINE_2", "This line comes from PARAMETER 2 !")
            .withStylesheet(Input.fromStream(resource("transformation.xsl")))
            .build()
            ;
        assertThat(got)
            .and(expected)
            .normalizeWhitespace()
            .ignoreComments()
            .areSimilar()
            ;
    }

    @Test
    @DisplayName("Simplified XMLUnit XSLT Transformation")
    void test_example_1() {
        Source expected = 
            Input.fromStream(resource("test-001-expected-1.xml"))
            .build()
            ;
        // simplified xslt transformations
        // https://github.com/xmlunit/user-guide/wiki/XSLT-Support
        Source got = 
            Input.byTransforming(Input.fromStream(resource("test-001-input.xml")))
            .withStylesheet(Input.fromStream(resource("transformation.xsl")))
            .build()
            ;
        assertThat(got)
            .and(expected)
            .normalizeWhitespace()
            .ignoreComments()
            .areSimilar()
            ;
    }

    private InputStream resource(String name) {
        return getClass().getClassLoader().getResourceAsStream(name);
    }
}
