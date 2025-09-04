package exceptions;

public class InvalidTransferAmountException extends Exception {
    public InvalidTransferAmountException() {
        super("Ошибка. Сумма перевода должна быть больше 0");
    }
}
