package exceptions;

public class LoginFailedException extends Exception{
    public LoginFailedException() {
        super("Ошибка. Имя и/или пароль не совпадают");
    }
}
