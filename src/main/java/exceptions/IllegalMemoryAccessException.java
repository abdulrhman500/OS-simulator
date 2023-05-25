package exceptions;

public class IllegalMemoryAccessException extends OperatingSystemException {
    public IllegalMemoryAccessException() {
        super("Illegal Action");
    }

    public IllegalMemoryAccessException(String message) {
        super(message);
    }
}
