package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.UserQueueExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.auth.LoginPage;
import guru.qa.niffler.page.auth.StartPage;
import guru.qa.niffler.page.main.MainPage;
import guru.qa.niffler.page.people.PeoplePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.chrome.ChromeOptions;

import static com.codeborne.selenide.Condition.text;
import static guru.qa.niffler.jupiter.annotation.User.Selector.FRIEND;
import static guru.qa.niffler.jupiter.annotation.User.Selector.INVITE_RECEIVED;
import static guru.qa.niffler.jupiter.annotation.User.Selector.INVITE_SENT;
import static guru.qa.niffler.jupiter.annotation.User.Selector.TEST_USER;

@WebTest
@ExtendWith(UserQueueExtension.class)
public class UsersQueueExampleTest {

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
    private final PeoplePage peoplePage = new PeoplePage();

    @BeforeEach
    void doLogin(@User(selector = TEST_USER) UserJson testUser) {
        Selenide.open("http://127.0.0.1:3000/");
        startPage.clickLoginButton();
        loginPage.singInNiffler(testUser.username(), testUser.testData().password());
    }

    @Test
    void checkInviteSentStatus(@User(selector = INVITE_SENT) UserJson testUser) {
        mainPage.iconPeople.click();
        SelenideElement peopleRecord = peoplePage.getUserByUserName(testUser.username());
        peopleRecord.shouldBe(text("Pending invitation"));
    }

    @Test
    void checkFriendStatus(@User(selector = FRIEND) UserJson testUser) {
        mainPage.iconPeople.click();
        SelenideElement peopleRecord = peoplePage.getUserByUserName(testUser.username());
        peopleRecord.shouldBe(text("You are friends"));
        peoplePage.checkIconClose(testUser.username());
    }

    @Test
    void checkInviteReceivedStatus(@User(selector = INVITE_RECEIVED) UserJson testUser) {
        mainPage.iconPeople.click();
        peoplePage.checkIconClose(testUser.username());
        peoplePage.checkIconSubmit(testUser.username());
    }
}
