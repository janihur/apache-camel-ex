// https://camel.apache.org/components/latest/others/main.html
import org.apache.camel.main.Main;

public class App {
    public static void main(String[] args) throws Exception {
        int case_ = 1;
        if (args.length > 0) {
            case_ = Integer.parseInt(args[0]);
        }

        Main main = new Main();

        switch (case_) {
            case 1:
                main.configure().addRoutesBuilder(Routes.routes1());
                break;
            case 2:
                main.configure().addRoutesBuilder(Routes.routes2());
                break;
            default:
                throw new Exception();
        }

        main.run(new String[]{"-duration", "5"});
    }
}
