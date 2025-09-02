import exceptions.InsufficientBalanceException;
import exceptions.InvalidRatingException;
import exceptions.InvalidTransferAmountException;
import exceptions.ItemNotFoundException;
import exceptions.LoginFailedException;
import exceptions.NegativeDepositException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    static Map<String, String> items = new HashMap<>();
    static Map<String, String> logins = new HashMap<>();
    static Map<String, BigDecimal> accounts = new HashMap<>();
    static Map<String, List<Integer>> products = new HashMap<>();

    public static void main(String[] args) throws IOException {
        System.out.println("Домашняя работа №10");

        //2. Проверка строки
        List<String> list = new ArrayList<>(List.of("aaa", "", " "));
        for (String s : list) {
            try {
                Main.nullString(s);
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка. Строка пуста или состоит только из пробелов");
            }
        }

        //4. Простая валидация возраста
        try {
            setAge(-5);
        } catch (IllegalArgumentException e) {
            System.out.printf("4. Простая валидация возраста - %s\n", e.getMessage());
        }

        //5. Собственное исключение: депозит
        try {
            deposit(5000);
        } catch (NegativeDepositException e) {
            System.out.println(e.getMessage());
        }

        try {
            deposit(-5);
        } catch (NegativeDepositException e) {
            System.out.println(e.getMessage());
        }

        //6. Поиск товара по коду
        items.put("101", "Карандаш");
        items.put("102", "Ручка");
        items.put("103", "Тетрадь");
        items.put("104", "Линейка");
        items.put("105", "Циркуль");
        try {
            System.out.println(getItem("101"));
        } catch (ItemNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println(getItem("106"));
        } catch (ItemNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println(getItem(""));
        } catch (ItemNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка. Задан пустой код");
        }

        //7. Чтение из файла
        System.out.println("7. Чтение из файла");
        System.out.println(readFile("src/exceptions/ItemNotFoundException.java"));
        System.out.println(readFile("ItemNotFoundException.java"));

        //8. Система логина
        logins.put("ivanov", "qwerty");
        logins.put("petrov", "qwerty");
        logins.put("sidorov", "qwerty");

        try{
            login("ivanov", "");
            System.out.println("Успех. Соединение установлено");
        } catch(LoginFailedException e){
            System.out.println(e.getMessage());
        }

        try{
            login("sidorov", "qwerty");
            System.out.println("Успех. Соединение установлено");
        } catch(LoginFailedException e){
            System.out.println(e.getMessage());
        }

        //9. Банковский перевод с валидацией
        accounts.put("x00001", BigDecimal.valueOf(1000));
        accounts.put("x00002", BigDecimal.valueOf(0));
        accounts.put("x00003", BigDecimal.valueOf(200));

        try {
            transfer("x00002", "x00001", 50);
        } catch (InvalidTransferAmountException | InsufficientBalanceException e) {
            System.out.println(e.getMessage());
        }

        try {
            transfer("x00001", "x00002", 55);
        } catch (InvalidTransferAmountException | InsufficientBalanceException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(accounts);

        //10.
        products.put("Простоквашино", new ArrayList<>());
        products.put("Вкуснотеево", new ArrayList<>());
        products.put("33 коровы", new ArrayList<>());

        try {
            rateProduct("Простоквашино", "4");
        } catch (InvalidRatingException e) {
            System.out.println(e.getMessage());
        }

        try {
            rateProduct("Вкуснотеево", "5");
            rateProduct("Простоквашино", "5");
        } catch (InvalidRatingException e) {
            System.out.println(e.getMessage());
        }

        try {
            rateProduct("Простоквашино", "-5");
        } catch (InvalidRatingException e) {
            System.out.println(e.getMessage());
        }

        try {
            rateProduct("Простоквашино", "AAA+");
        } catch (InvalidRatingException e) {
            System.out.println(e.getMessage());
        }

        try {
            rateProduct("33 коровы", "");
        } catch (InvalidRatingException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(products);
    }

    /**
     * 1. Безопасное деление
     * Напишите метод safeDivide(int a, int b), который возвращает a / b.
     * Если b == 0, перехватите исключение и выведите сообщение: "Деление на ноль запрещено".
     */
    public static long safeDivide(int a, int b) {
        try {
            return (long) a / b;
        }
        catch (ArithmeticException e) {
            System.out.println("Ошибка. Деление на ноль запрещено");
            return b;
        }
    }

    /**
     * 2. Проверка строки
     * Напишите метод, который принимает строку и выбрасывает IllegalArgumentException,
     * если строка пуста или состоит только из пробелов
     */
    public static void nullString(String str) {
        if (str.trim().isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 3. Преобразование строки в число
     * Дан список строк List.of("10", "abc", "5").
     * Преобразуйте его в список чисел, перехватывая NumberFormatException.
     * Ошибки не должны останавливать выполнение.
     */
    public static List<Integer> strListToInt(List<String> strings) {
        List<Integer> numbers = new ArrayList<>();
        for (String s : strings) {
            try {
                numbers.add(Integer.valueOf(s));
            } catch (NumberFormatException e) {
                System.out.printf("Ошибка. \"%s\" не является числом\n", s);
            }
        }
        return numbers;
    }

    /**
     * 4. Простая валидация возраста
     * Метод setAge(int age) должен выбрасывать IllegalArgumentException, если возраст меньше нуля.
     * Обработайте исключение и выведите сообщение.
     */
    public static void setAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Ошибка. Возраст меньше 0");
        }
    }

    /**
     * 5. Собственное исключение: депозит
     * Создайте исключение NegativeDepositException, и метод deposit(double amount),
     * который выбрасывает это исключение при отрицательном значении.
     * Обработайте его в main.
     */
    public static void deposit(double amount) throws NegativeDepositException {
        if (amount < 0) {
            throw new NegativeDepositException("Ошибка. Сумма не может быть отрицательной");
        }

        System.out.printf("Успех. Сумма %s обработана\n", amount);
    }

    /**
     * 6. Поиск товара по коду
     * Реализуйте метод getItem(String code).
     * Если код не найден в карте товаров, выбросите ItemNotFoundException, унаследованное от RuntimeException.
     * Продемонстрируйте поведение в main.
     */
    public static String getItem(String code) {
        nullString(code);
        if (!items.containsKey(code)) {
            throw new ItemNotFoundException("Ошибка. Код \"" + code + "\" не найден в карте товаров");
        }

        return items.get(code);
    }

    /**
     * 7. Чтение из файла
     * Реализуйте метод readFile(String path), который читает текстовый файл и возвращает список строк.
     * Используйте BufferedReader, перехватите IOException, выведите сообщение об ошибке.
     */
    public static List<String> readFile(String path)  {
        List<String> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String s;
            while ((s = br.readLine()) != null) {
                list.add(s + "\n");
            }
        } catch (IOException e) {
            System.out.println("Ошибка. " + e.getMessage());
        }
        return list;
    }

    /**
     * 8. Система логина
     * Создайте метод login(String username, String password), в котором логин и пароль проверяются на корректность.
     * Если один из них не совпадает — выбрасывается LoginFailedException.
     * Исключение должно наследоваться от Exception.
     */
    public static void login(String username, String password) throws LoginFailedException {
        if (!logins.containsKey(username) || !logins.get(username).equals(password)) {
            throw new LoginFailedException("Ошибка. Имя и/или пароль не совпадают");
        }
    }

    /**
     * 9. Банковский перевод с валидацией
     * Метод transfer(fromAccount, toAccount, amount):
     * выбрасывает InvalidTransferAmountException, если сумма <= 0
     * выбрасывает InsufficientBalanceException, если баланс отправителя меньше суммы
     * содержит try-catch в main
     */
    public static void transfer(String fromAccount, String toAccount, double amount)
        throws InvalidTransferAmountException, InsufficientBalanceException {

        if (amount <= 0) {
            throw new InvalidTransferAmountException("InvalidTransferAmountException");
        }

        if (accounts.get(fromAccount).compareTo(BigDecimal.valueOf(amount)) < 0) {
            throw new InsufficientBalanceException("Ошибка. Баланс отправителя меньше суммы " + amount);
        }

        accounts.put(fromAccount, accounts.get(fromAccount).subtract(BigDecimal.valueOf(amount)));
        accounts.put(toAccount, accounts.get(toAccount).add(BigDecimal.valueOf(amount)));
    }

    /**
     * 10. Сервис оценки товара
     * Реализуйте метод rateProduct(String rating), который:
     * принимает значение от 1 до 5
     * выбрасывает InvalidRatingException (checked), если значение вне диапазона
     * сохраняет рейтинг в списке, если всё хорошо
     * также перехватывает NumberFormatException, если рейтинг пришёл в виде строки, но содержит нечисловое значение
     */
    public static void rateProduct(String product, String rating)
        throws InvalidRatingException {
        try {
            int rate = Integer.parseInt(rating);
            if ((rate < 1) || (rate > 5)) {
                throw new InvalidRatingException("Ошибка. Значение вне диапазона");
            }

            products.computeIfAbsent(product, k -> new ArrayList<>()).add(rate);

        } catch (NumberFormatException e) {
            System.out.println("Ошибка. Задайте числовое значение от 1 до 5");
        }
    }


}