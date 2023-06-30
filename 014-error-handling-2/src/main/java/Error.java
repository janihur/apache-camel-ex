public class Error extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final int errorCode;
    public Error(String message) {
        super(message);
        this.errorCode = 0;
    }
    public Error(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}