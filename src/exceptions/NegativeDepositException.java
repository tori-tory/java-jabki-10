package exceptions;

public class NegativeDepositException extends Exception{
    public NegativeDepositException() {
        super("Ошибка. Сумма не может быть отрицательной");
    }
}
