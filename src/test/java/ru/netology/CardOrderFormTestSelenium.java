package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardOrderFormTestSelenium {

    private static ChromeOptions options;
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
        options = new ChromeOptions();
        options.addArguments("--headless");
    }

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        if(driver !=null) {
            driver.quit();
        }
    }

    @Test
    void shouldSubmitRequestIfFieldsAreFilledCorrect() {
        driver.findElement(By.cssSelector("[data-test-id=\"name\"] input")).sendKeys("Василий Васин");
        driver.findElement(By.cssSelector("[data-test-id=\"phone\"] input")).sendKeys("+79990000000");
        driver.findElement(By.cssSelector("[class='checkbox__box']")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();

        String actualMassage = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", actualMassage.trim());
    }

    @Test
    void shouldNotSubmitRequestIfTextFieldIsEmpty() {
        driver.findElement(By.cssSelector("[data-test-id=\"name\"] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id=\"phone\"] input")).sendKeys("+79990000000");
        driver.findElement(By.cssSelector("[class='checkbox__box']")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();

        String actualMassage = driver.findElement(By.cssSelector("[data-test-id=name] .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", actualMassage.trim());
    }

    @Test
    void shouldNotSubmitRequestIfTextFieldIsInvalid() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=\"name\"] input")).sendKeys("jdhfh");
        driver.findElement(By.cssSelector("[data-test-id=\"phone\"] input")).sendKeys("+79990000000");
        driver.findElement(By.cssSelector("[class='checkbox__box']")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();

        String actualMassage = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", actualMassage.trim());
    }

    @Test
    void shouldNotSubmitRequestIfTelFieldIsEmpty() {
        driver.findElement(By.cssSelector("[data-test-id=\"name\"] input")).sendKeys("Василий Васин");
        driver.findElement(By.cssSelector("[data-test-id=\"phone\"] input")).sendKeys("");
        driver.findElement(By.cssSelector("[class='checkbox__box']")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();

        String actualMassage = driver.findElement(By.cssSelector("[data-test-id=\"phone\"].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", actualMassage.trim());
    }

    @Test
    void shouldNotSubmitRequestIfTelFieldIsInvalid() {
        driver.findElement(By.cssSelector("[data-test-id=\"name\"] input")).sendKeys("Василий Васин");
        driver.findElement(By.cssSelector("[data-test-id=\"phone\"] input")).sendKeys("9374573");
        driver.findElement(By.cssSelector("[class='checkbox__box']")).click();
        driver.findElement(By.cssSelector("[type='button']")).click();

        String actualMassage = driver.findElement(By.cssSelector("[data-test-id=\"phone\"].input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", actualMassage.trim());
    }

    @Test
    void shouldNotSubmitRequestIfCheckboxNotClick() {
        driver.findElement(By.cssSelector("[data-test-id=\"name\"] input")).sendKeys("Василий Васин");
        driver.findElement(By.cssSelector("[data-test-id=\"phone\"] input")).sendKeys("+79990000000");
        driver.findElement(By.cssSelector("[class='checkbox__box']"));
        driver.findElement(By.cssSelector("[type='button']")).click();

        String actualMassage = driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid .checkbox__text")).getText();
        String expectedMessage = "Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";
        assertEquals(expectedMessage, actualMassage.trim());

    }

}
