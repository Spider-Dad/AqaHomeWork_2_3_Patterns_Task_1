package ru.netology.delivery.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.delivery.data.DataGenerator;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        // TODO: добавить логику теста в рамках которого будет выполнено планирование и перепланирование встречи.
        // Для заполнения полей формы можно использовать пользователя validUser и строки с датами в переменных
        // firstMeetingDate и secondMeetingDate. Можно также вызывать методы generateCity(locale),
        // generateName(locale), generatePhone(locale) для генерации и получения в тесте соответственно города,
        // имени и номера телефона без создания пользователя в методе generateUser(String locale) в датагенераторе

        // Планирование первой встречи

        $("[data-test-id=city] input").sendKeys(validUser.getCity());
        $(".menu").$(byText(validUser.getCity())).click();

        $("[data-test-id=date] input").doubleClick().sendKeys(firstMeetingDate);
        $("[data-test-id=date] input").shouldHave(value(firstMeetingDate));

        $("[data-test-id=name] input").sendKeys(validUser.getName());
        $("[data-test-id=name] input").shouldHave(value(validUser.getName()));


        $("[data-test-id=phone] input").sendKeys(validUser.getPhone());
        $("[data-test-id=phone] input").shouldHave(value(validUser.getPhone()));

        $("[data-test-id=agreement]").click();
        $("[data-test-id=agreement] input").shouldBe(selected);

        $("div.grid-col button.button_view_extra").click();

        $("[data-test-id=success-notification] .notification__content").shouldHave(text("Встреча успешно запланирована на " + firstMeetingDate));

        // Планирование второй встречи

    }
}
