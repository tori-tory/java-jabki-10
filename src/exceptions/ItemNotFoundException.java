package exceptions;

public class ItemNotFoundException extends RuntimeException{
    public ItemNotFoundException() {
        super("Ошибка. Код не найден в карте товаров");
    }
}
