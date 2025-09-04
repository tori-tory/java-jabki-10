import exceptions.InvalidRatingException;
import exceptions.ItemNotFoundException;
import exceptions.LoginFailedException;
import exceptions.NegativeDepositException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class MainTest {

    @Test
    public void testSafeDivide() {
        Assertions.assertEquals(2.0, Main.safeDivide(10, 5));
        Assertions.assertEquals(0.0, Main.safeDivide(5, 0));
        Assertions.assertEquals(2147483648D, Main.safeDivide(Integer.MIN_VALUE,-1));
    }

    @Test
    public void testNullStringNotThrow() {
        Assertions.assertDoesNotThrow(() -> Main.nullString("555"));
     }

    @Test
    public void testNullStringThrows() {
        Assertions.assertThrows(IllegalArgumentException.class,  () -> Main.nullString(null));
        Assertions.assertThrows(IllegalArgumentException.class,  () -> Main.nullString(""));
        Assertions.assertThrows(IllegalArgumentException.class,  () -> Main.nullString("   "));
    }

    @Test
    public void testStrListToInt() {
        List<String> strings = new ArrayList<>(List.of("10", "abc", "5"));
        System.out.println(strings);
        List<Integer> numbers = Main.strListToInt(strings);
        System.out.println(numbers);
        Assertions.assertEquals(List.of(10,5), numbers);
    }

    @Test
    public void testStrListToIntEmpty() {
        List<String> strings = new ArrayList<>(List.of());
        System.out.println(strings);
        List<Integer> numbers = Main.strListToInt(strings);
        System.out.println(numbers);
        Assertions.assertTrue(numbers.isEmpty());
    }

    @Test
    public void testStrListToIntInvalid() {
        List<String> strings = new ArrayList<>(List.of("a", "b", "c"));
        System.out.println(strings);
        List<Integer> numbers = Main.strListToInt(strings);
        System.out.println(numbers);
        Assertions.assertTrue(numbers.isEmpty());
    }

    @Test
    void testSetAgeValid() {
        Assertions.assertDoesNotThrow(() -> Main.setAge(25));
    }

    @Test
    void testSetAgeZero() {
        Assertions.assertDoesNotThrow(() -> Main.setAge(0));
    }

    @Test
    void testSetAgeNegative() {
        IllegalArgumentException e = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> Main.setAge(-5)
        );
        Assertions.assertEquals("Ошибка. Возраст меньше 0", e.getMessage());
    }

    @Test
    void testDepositNegative() {
        NegativeDepositException e = Assertions.assertThrows(
                NegativeDepositException.class,
                () -> Main.deposit(-5)
        );
        Assertions.assertEquals("Ошибка. Сумма не может быть отрицательной", e.getMessage());
    }

    @Test
    void testGetItemExists() {
        Main.items.put("A101","Гладиолус");
        Assertions.assertEquals("Гладиолус", Main.getItem("A101"));
    }

    @Test
    void testGetItemNotExists() {
        Main.items.put("A101","Гладиолус");
        RuntimeException ex = Assertions.assertThrows(
                ItemNotFoundException.class,
                () -> Main.getItem("101")
        );
        Assertions.assertEquals("Ошибка. Код \"101\" не найден в карте товаров", ex.getMessage());
    }

    @Test
    void testReadNonExistingFile() {
        Assertions.assertTrue(Main.readFile("tmp.txt").isEmpty());
    }

    @Test
    void testReadExistingFile() throws IOException {
        Path tempFile = Files.createTempFile("tmp", ".txt");
        try  {
            try (PrintWriter writer = new PrintWriter(tempFile.toFile())) {
                writer.println("Всем");
                writer.println("привет!");
            }
            List<String> list = Main.readFile(tempFile.toString());
            Assertions.assertEquals(List.of("Всем\n","привет!\n"), list);
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    void testLoginInvalid() throws LoginFailedException {
        Main.logins.put("Smit", "qwerty");
        LoginFailedException e = Assertions.assertThrows(
                LoginFailedException.class,
                () -> Main.login("Smit","Smit")
        );
        Assertions.assertEquals("Ошибка. Имя и/или пароль не совпадают", e.getMessage());
    }

    @BeforeEach
    void clearRateProduct() {
        Main.products.clear();
    }

    @Test
    void testRateProductValid() throws InvalidRatingException {
        Main.rateProduct("Parmalat","5");
        Assertions.assertEquals(List.of(5), Main.products.get("Parmalat"));
        Main.rateProduct("Parmalat","2");
        Assertions.assertEquals(List.of(5,2), Main.products.get("Parmalat"));
        Main.rateProduct("Parmalat","5");
        Assertions.assertEquals(List.of(5,2,5), Main.products.get("Parmalat"));

        System.out.println(Main.products.get("Parmalat"));
        System.out.println(Main.products);
    }

    @Test
    void testRateProductInvalidRange() throws InvalidRatingException {
        Main.products.put("Parmalat", List.of());
        InvalidRatingException e = Assertions.assertThrows(
                InvalidRatingException.class,
                () -> Main.rateProduct("Parmalat","100")
        );
        Assertions.assertEquals("Ошибка. Задайте числовое значение от 1 до 5", e.getMessage());
        Assertions.assertTrue(Main.products.get("Parmalat").isEmpty());
    }

    @Test
    void tesRateProductInvalidNumberFormat() throws InvalidRatingException {
        Main.rateProduct("Parmalat","abc");
        Assertions.assertTrue(Main.products.isEmpty());
    }
}