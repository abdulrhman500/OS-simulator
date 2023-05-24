package exceptions;

public class IllegalMemoryAccessException extends Exception {
    public IllegalMemoryAccessException() {
        super("Illegal Action");
    }

    public IllegalMemoryAccessException(String message) {
        super(message);
    }
}
