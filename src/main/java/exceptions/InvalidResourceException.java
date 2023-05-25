package exceptions;

public class InvalidResourceException extends OperatingSystemException{
    public InvalidResourceException() {super();}
    public InvalidResourceException(String message) {
        super(message);
    }
}
