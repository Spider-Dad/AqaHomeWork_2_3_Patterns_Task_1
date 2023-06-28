package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.delivery.data.DataGenerator;

import static com.codeborne.selenide.Condition.*;
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

        $("[data-test-id=city] input").sendKeys(validUser.getCity());
        $("[data-test-id=city] input").shouldHave(Condition.value(validUser.getCity()));

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

        $("[data-test-id=date] input").doubleClick().sendKeys(secondMeetingDate);
        $("[data-test-id=date] input").shouldHave(value(secondMeetingDate));

        $("div.grid-col button.button_view_extra").click();

        $("[data-test-id=replan-notification]").shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $("[data-test-id=replan-notification] button.button").click();

        $("[data-test-id=success-notification] .notification__content").shouldHave(text("Встреча успешно запланирована на " + secondMeetingDate));
    }

    @Test
    @DisplayName("Not should successful plan  meeting if not correct latin city")
    void shouldNotSuccessfulPlanMeetingNotCorrectLatinCity() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var invalidCity = "Saratov";

        $("[data-test-id=city] input").sendKeys(invalidCity);

        $("[data-test-id=date] input").doubleClick().sendKeys(firstMeetingDate);
        $("[data-test-id=date] input").shouldHave(value(firstMeetingDate));

        $("[data-test-id=name] input").sendKeys(validUser.getName());
        $("[data-test-id=name] input").shouldHave(value(validUser.getName()));

        $("[data-test-id=phone] input").sendKeys(validUser.getPhone());
        $("[data-test-id=phone] input").shouldHave(value(validUser.getPhone()));

        $("[data-test-id=agreement]").click();
        $("[data-test-id=agreement] input").shouldBe(selected);

        $("div.grid-col button.button_view_extra").click();

        String errorMessage = "Доставка в выбранный город недоступна";
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(text(errorMessage));

        Assertions.assertEquals(errorMessage, $("[data-test-id=city].input_invalid .input__sub").getText());

    }

    @Test
    @DisplayName("Not should successful plan  meeting if not correct  city")
    void shouldNotSuccessfulPlanMeetingNotCorrectCity() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var invalidCity = "Париж";

        $("[data-test-id=city] input").sendKeys(invalidCity);

        $("[data-test-id=date] input").doubleClick().sendKeys(firstMeetingDate);
        $("[data-test-id=date] input").shouldHave(value(firstMeetingDate));

        $("[data-test-id=name] input").sendKeys(validUser.getName());
        $("[data-test-id=name] input").shouldHave(value(validUser.getName()));

        $("[data-test-id=phone] input").sendKeys(validUser.getPhone());
        $("[data-test-id=phone] input").shouldHave(value(validUser.getPhone()));

        $("[data-test-id=agreement]").click();
        $("[data-test-id=agreement] input").shouldBe(selected);

        $("div.grid-col button.button_view_extra").click();

        String errorMessage = "Доставка в выбранный город недоступна";
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(text(errorMessage));

        Assertions.assertEquals(errorMessage, $("[data-test-id=city].input_invalid .input__sub").getText());

    }

    @Test
    @DisplayName("Not should successful plan  meeting if not filled city")
    void shouldNotSuccessfulPlanMeetingNotFilledCity() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);

        $("[data-test-id=date] input").doubleClick().sendKeys(firstMeetingDate);
        $("[data-test-id=date] input").shouldHave(value(firstMeetingDate));

        $("[data-test-id=name] input").sendKeys(validUser.getName());
        $("[data-test-id=name] input").shouldHave(value(validUser.getName()));

        $("[data-test-id=phone] input").sendKeys(validUser.getPhone());
        $("[data-test-id=phone] input").shouldHave(value(validUser.getPhone()));

        $("[data-test-id=agreement]").click();
        $("[data-test-id=agreement] input").shouldBe(selected);

        $("div.grid-col button.button_view_extra").click();

        String errorMessage = "Поле обязательно для заполнения";
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(text(errorMessage));

        Assertions.assertEquals(errorMessage, $("[data-test-id=city].input_invalid .input__sub").getText());

    }

    @Test
    @DisplayName("Not should successful plan  meeting if meeting day less 3 days")
    void shouldNotSuccessfulPlanMeetingDayLessThree() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 2;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);

        $("[data-test-id=city] input").sendKeys(validUser.getCity());
        $("[data-test-id=city] input").shouldHave(Condition.value(validUser.getCity()));

        $("[data-test-id=date] input").doubleClick().sendKeys(firstMeetingDate);
        $("[data-test-id=date] input").shouldHave(value(firstMeetingDate));

        $("[data-test-id=name] input").sendKeys(validUser.getName());
        $("[data-test-id=name] input").shouldHave(value(validUser.getName()));

        $("[data-test-id=phone] input").sendKeys(validUser.getPhone());
        $("[data-test-id=phone] input").shouldHave(value(validUser.getPhone()));

        $("[data-test-id=agreement]").click();
        $("[data-test-id=agreement] input").shouldBe(selected);

        $("div.grid-col button.button_view_extra").click();

        String errorMessage = "Заказ на выбранную дату невозможен";
        $("[data-test-id=date] .input_invalid .input__sub").shouldHave(text(errorMessage));

        Assertions.assertEquals(errorMessage, $("[data-test-id=date] .input_invalid .input__sub").getText());

    }

    @Test
    @DisplayName("Not should successful plan  meeting if meeting day not correct format ")
    void shouldNotSuccessfulPlanMeetingDayNotCorrectFormat() {
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id=city] input").sendKeys(validUser.getCity());
        $("[data-test-id=city] input").shouldHave(Condition.value(validUser.getCity()));

        $("[data-test-id=date] input").doubleClick().sendKeys("10.10.23");
        $("[data-test-id=date] input").shouldHave(value("10.10.23"));

        $("[data-test-id=name] input").sendKeys(validUser.getName());
        $("[data-test-id=name] input").shouldHave(value(validUser.getName()));

        $("[data-test-id=phone] input").sendKeys(validUser.getPhone());
        $("[data-test-id=phone] input").shouldHave(value(validUser.getPhone()));

        $("[data-test-id=agreement]").click();
        $("[data-test-id=agreement] input").shouldBe(selected);

        $("div.grid-col button.button_view_extra").click();

        String errorMessage = "Неверно введена дата";
        $("[data-test-id=date] .input_invalid .input__sub").shouldHave(text(errorMessage));

        Assertions.assertEquals(errorMessage, $("[data-test-id=date] .input_invalid .input__sub").getText());

    }

    @Test
    @DisplayName("Not should successful plan  meeting if not filled phone")
    void shouldNotSuccessfulPlanMeetingNotFilledPhone() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);

        $("[data-test-id=city] input").sendKeys(validUser.getCity());
        $("[data-test-id=city] input").shouldHave(Condition.value(validUser.getCity()));

        $("[data-test-id=date] input").doubleClick().sendKeys(firstMeetingDate);
        $("[data-test-id=date] input").shouldHave(value(firstMeetingDate));

        $("[data-test-id=name] input").sendKeys(validUser.getName());
        $("[data-test-id=name] input").shouldHave(value(validUser.getName()));

        $("[data-test-id=agreement]").click();
        $("[data-test-id=agreement] input").shouldBe(selected);

        $("div.grid-col button.button_view_extra").click();

        String errorMessage = "Поле обязательно для заполнения";
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(text(errorMessage));

        Assertions.assertEquals(errorMessage, $("[data-test-id=phone].input_invalid .input__sub").getText());

    }

    @Test
    @DisplayName("Not should successful plan  meeting if not filled Name")
    void shouldNotSuccessfulPlanMeetingNotFilledName() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);

        $("[data-test-id=city] input").sendKeys(validUser.getCity());
        $("[data-test-id=city] input").shouldHave(Condition.value(validUser.getCity()));

        $("[data-test-id=date] input").doubleClick().sendKeys(firstMeetingDate);
        $("[data-test-id=date] input").shouldHave(value(firstMeetingDate));

        $("[data-test-id=phone] input").sendKeys(validUser.getPhone());
        $("[data-test-id=phone] input").shouldHave(value(validUser.getPhone()));

        $("[data-test-id=agreement]").click();
        $("[data-test-id=agreement] input").shouldBe(selected);

        $("div.grid-col button.button_view_extra").click();

        String errorMessage = "Поле обязательно для заполнения";
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(text(errorMessage));

        Assertions.assertEquals(errorMessage, $("[data-test-id=name].input_invalid .input__sub").getText());

    }

    @Test
    @DisplayName("Not should successful plan  meeting if not push agreement")
    void shouldNotSuccessfulPlanMeetingNotPushAgreement() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);

        $("[data-test-id=city] input").sendKeys(validUser.getCity());
        $("[data-test-id=city] input").shouldHave(Condition.value(validUser.getCity()));

        $("[data-test-id=date] input").doubleClick().sendKeys(firstMeetingDate);
        $("[data-test-id=date] input").shouldHave(value(firstMeetingDate));

        $("[data-test-id=name] input").sendKeys(validUser.getName());
        $("[data-test-id=name] input").shouldHave(value(validUser.getName()));

        $("[data-test-id=phone] input").sendKeys(validUser.getPhone());
        $("[data-test-id=phone] input").shouldHave(value(validUser.getPhone()));

        $("div.grid-col button.button_view_extra").click();

        Assertions.assertTrue($("[data-test-id=agreement].input_invalid .checkbox__box").isDisplayed());

    }

    @Test
    @DisplayName("Not should successful plan meeting with invalid phone number")
    void shouldNotSuccessfulPlanMeetingWithInvalidPhoneNumber() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);

        $("[data-test-id=city] input").sendKeys(validUser.getCity());
        $("[data-test-id=city] input").shouldHave(Condition.value(validUser.getCity()));

        $("[data-test-id=date] input").doubleClick().sendKeys(firstMeetingDate);
        $("[data-test-id=date] input").shouldHave(value(firstMeetingDate));

        $("[data-test-id=name] input").sendKeys(validUser.getName());
        $("[data-test-id=name] input").shouldHave(Condition.value(validUser.getName()));

        // Enter invalid phone number
        $("[data-test-id=phone] input").sendKeys("+1 900 123 45 6");
        $("[data-test-id=phone] input").shouldHave(Condition.value("+1 900 123 45 6"));

        $("[data-test-id=agreement]").click();
        $("[data-test-id=agreement] input").shouldBe(Condition.selected);

        $("div.grid-col button.button_view_extra").click();

        $("[data-test-id=success-notification] .notification__content").shouldNot(text("Встреча успешно запланирована на " + firstMeetingDate));
    }

}
