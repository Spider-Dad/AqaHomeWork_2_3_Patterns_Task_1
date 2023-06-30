package ru.netology.delivery.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
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

        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();
        $(byText("Успешно!")).shouldBe(visible, Duration.ofSeconds(10));

        $("[data-test-id=success-notification] .notification__content").shouldHave(text("Встреча успешно запланирована на " + firstMeetingDate)).shouldBe(visible);

        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(secondMeetingDate);
        $(byText("Запланировать")).click();

        $("[data-test-id=replan-notification]").shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?")).shouldBe(visible);
        $("[data-test-id=replan-notification] button").click();

        $("[data-test-id=success-notification] .notification__content").shouldHave(text("Встреча успешно запланирована на " + secondMeetingDate)).shouldBe(visible);
    }

    @Test
    @DisplayName("Not should successful plan  meeting if not correct latin city")
    void shouldNotSuccessfulPlanMeetingNotCorrectLatinCity() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var invalidCity = "Saratov";

        $("[data-test-id=city] input").setValue(invalidCity);

        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();

        String errorMessage = "Доставка в выбранный город недоступна";
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(text(errorMessage));
        Assertions.assertEquals(errorMessage, $("[data-test-id=city].input_invalid .input__sub").getText().trim());
    }

    @Test
    @DisplayName("Not should successful plan  meeting if not correct  city")
    void shouldNotSuccessfulPlanMeetingNotCorrectCity() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var invalidCity = "Париж";

        $("[data-test-id=city] input").setValue(invalidCity);
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();

        String errorMessage = "Доставка в выбранный город недоступна";
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(text(errorMessage));
        Assertions.assertEquals(errorMessage, $("[data-test-id=city].input_invalid .input__sub").getText().trim());

    }

    @Test
    @DisplayName("Not should successful plan  meeting if not filled city")
    void shouldNotSuccessfulPlanMeetingNotFilledCity() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);

        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();

        String errorMessage = "Поле обязательно для заполнения";
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(text(errorMessage));
        Assertions.assertEquals(errorMessage, $("[data-test-id=city].input_invalid .input__sub").getText().trim());
    }

    @Test
    @DisplayName("Not should successful plan  meeting if meeting day less 3 days")
    void shouldNotSuccessfulPlanMeetingDayLessThree() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 2;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);

        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();

        String errorMessage = "Заказ на выбранную дату невозможен";
        $("[data-test-id=date] .input_invalid .input__sub").shouldHave(text(errorMessage));
        Assertions.assertEquals(errorMessage, $("[data-test-id=date] .input_invalid .input__sub").getText().trim());

    }

    @Test
    @DisplayName("Not should successful plan  meeting if meeting day not correct format ")
    void shouldNotSuccessfulPlanMeetingDayNotCorrectFormat() {
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue("10.10.23");
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();

        String errorMessage = "Неверно введена дата";
        $("[data-test-id=date] .input_invalid .input__sub").shouldHave(text(errorMessage));
        Assertions.assertEquals(errorMessage, $("[data-test-id=date] .input_invalid .input__sub").getText().trim());

    }

    @Test
    @DisplayName("Not should successful plan  meeting if not filled phone")
    void shouldNotSuccessfulPlanMeetingNotFilledPhone() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);

        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();

        String errorMessage = "Поле обязательно для заполнения";
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(text(errorMessage));
        Assertions.assertEquals(errorMessage, $("[data-test-id=phone].input_invalid .input__sub").getText().trim());
    }

    @Test
    @DisplayName("Not should successful plan  meeting if not filled Name")
    void shouldNotSuccessfulPlanMeetingNotFilledName() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);

        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();

        String errorMessage = "Поле обязательно для заполнения";
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(text(errorMessage));
        Assertions.assertEquals(errorMessage, $("[data-test-id=name].input_invalid .input__sub").getText().trim());

    }

    @Test
    @DisplayName("Not should successful plan  meeting if not filled Date")
    void shouldNotSuccessfulPlanMeetingNotFilledDate() {
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();

        String errorMessage = "Неверно введена дата";
        $("[data-test-id=date] .input_invalid").shouldHave(text(errorMessage));
        Assertions.assertEquals(errorMessage, $("[data-test-id=date] .input_invalid").getText().trim());

    }

    @Test
    @DisplayName("Not should successful plan  meeting if not push agreement")
    void shouldNotSuccessfulPlanMeetingNotPushAgreement() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);

        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $(byText("Запланировать")).click();

        Assertions.assertTrue($("[data-test-id=agreement].input_invalid .checkbox__box").isDisplayed());

    }
}
