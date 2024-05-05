package guru.qa.niffler.page.people;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;

public class PeoplePage {

    private final ElementsCollection listPeople = $$(".table.abstract-table tbody tr");

    public SelenideElement getUserByUserName(String username) {
        return listPeople.find(text(username));
    }

    public void checkIconClose(String username) {
        getUserByUserName(username).find(".button-icon.button-icon_type_close").shouldBe(visible);
    }

    public void checkIconSubmit(String username) {
        getUserByUserName(username).find(".button-icon.button-icon_type_submit").shouldBe(visible);
    }
}
