package guru.qa.niffler.page.auth;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private final SelenideElement usernameField = $("input[name='username']");
    private final SelenideElement passwordField = $("input[name='password']");
    private final SelenideElement singInButton = $("button[type='submit']");

    public void singInNiffler(String username, String password) {
        usernameField.setValue(username);
        passwordField.setValue(password);
        singInButton.click();
    }
}
