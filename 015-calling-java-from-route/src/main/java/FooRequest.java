public class FooRequest {
    private String id;

    public FooRequest(String id) {
        this.id = id;
    }

    public String request() {
        return "This is FooRequest " + this.id;
    }
}
