package exceptions;

public class InvalidTransferAmountException extends Exception {
    public InvalidTransferAmountException(String message) {
        super(message);
    }
}
