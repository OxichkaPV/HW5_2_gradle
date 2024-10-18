package ru.netology.ibank.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.ibank.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


public class AuthorizationTest {
    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Success: user registered")
    void registeredUserAuthorization() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("button.button").click();
        $("h2").shouldHave(Condition.exactText("Личный кабинет")).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Error: user blocked")
    void registeredUserIsBlocked() {
        var blockedUser = DataGenerator.Registration.getRegisteredUser("blocked");
        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Пользователь заблокирован"), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Error: Incorrect user login entered")
    void incorrectRegisteredUserLogin() {
        var incorrectUser = DataGenerator.Registration.getRegisteredUser("active");
        var incorrectLogin = DataGenerator.generateLogin();
        $("[data-test-id='login'] input").setValue(incorrectLogin);
        $("[data-test-id='password'] input").setValue(incorrectUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! неверно указан логин или пароль"), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Error: Incorrect user password entered")
    void incorrectRegisteredUserPassword() {
        var incorrectUser = DataGenerator.Registration.getRegisteredUser("active");
        var incorrectPassword = DataGenerator.generatePassword();
        $("[data-test-id='login'] input").setValue(incorrectUser.getLogin());
        $("[data-test-id='password'] input").setValue(incorrectPassword);
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! неверно указан логин или пароль"), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Error: Unregistered user")
    void unregisteredUser() {
        var incorrectUser = DataGenerator.Registration.getUser("active");
        $("[data-test-id='login'] input").setValue(incorrectUser.getLogin());
        $("[data-test-id='password'] input").setValue(incorrectUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! неверно указан логин или пароль"), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }
}
