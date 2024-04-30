package guru.qa.niffler.page.auth;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class StartPage {

    private final SelenideElement loginButton = $("a[href*='redirect']");

    public void clickLoginButton() {
        loginButton.click();
    }
}
