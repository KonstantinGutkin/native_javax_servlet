package exceptions;

public class ApplicationException extends RuntimeException {

    public ApplicationException() {
        super("Application has error");
    }
}
