package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.jupiter.extension.CategoryExtension;
import guru.qa.niffler.jupiter.extension.SpendExtension;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.auth.LoginPage;
import guru.qa.niffler.page.auth.StartPage;
import guru.qa.niffler.page.main.MainPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.chrome.ChromeOptions;

@ExtendWith({CategoryExtension.class, SpendExtension.class})
public class SpendingTest {

    static {
        Configuration.browserSize = "1920x1080";
        Configuration.browser = "chrome";
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--incognito");
        Configuration.browserCapabilities = chromeOptions;
    }

    private final StartPage startPage = new StartPage();
    private final LoginPage loginPage = new LoginPage();
    private final MainPage mainPage = new MainPage();

    @BeforeEach
    void doLogin() {
        Selenide.open("http://127.0.0.1:3000/");
        startPage.clickLoginButton();
        loginPage.singInNiffler("Ale", "1234");
    }

    @Category(
            category = "Обучение",
            username = "Ale"
    )
    @Spend(
            username = "Ale",
            description = "QA.GURU Advanced 5",
            amount = 65000.00,
            currency = CurrencyValues.RUB,
            category = "Обучение"
    )
    @Test
    void spendingShouldBeDeletedAfterTableAction(SpendJson spendJson) {
        SelenideElement rowWithSpending = mainPage.getSpend(spendJson.description());
        mainPage.deleteSpend(rowWithSpending);
        mainPage.checkSpendNull();
    }
}
