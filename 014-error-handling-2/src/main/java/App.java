// https://camel.apache.org/components/latest/others/main.html
import org.apache.camel.main.Main;

public class App {
    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.configure().addRoutesBuilder(Routes.routes());
        main.run(new String[]{"-duration", "20"});
    }
}
