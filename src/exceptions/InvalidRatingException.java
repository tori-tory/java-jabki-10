package exceptions;

public class InvalidRatingException extends Exception{
    public InvalidRatingException() {
        super("Ошибка. Задайте числовое значение от 1 до 5");
    }
}
