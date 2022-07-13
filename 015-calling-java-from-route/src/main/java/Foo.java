import org.apache.camel.Processor;

public class Foo {
    FooRequest request;
    Processor responseProcessor;

    public Foo(FooRequest request, Processor responseProcessor) {
        this.request = request;
        this.responseProcessor = responseProcessor;
    }

    public FooRequest getRequest() {
        return request;
    }

    public Processor getResponseProcessor() {
        return responseProcessor;
    }
}
