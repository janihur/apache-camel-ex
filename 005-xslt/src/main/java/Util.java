import java.io.InputStream;
import java.util.Scanner;

public class Util {
    public String getMsg1() {
        return convertStreamToString(readResourceFile("msg1.xml"));
    }

    public String convertStreamToString(java.io.InputStream in) {
        java.util.Scanner s = new java.util.Scanner(in).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public InputStream readResourceFile(String filename) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = classLoader.getResourceAsStream(filename);
        return in;
    }
}
