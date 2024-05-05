package guru.qa.niffler.page.main;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    private final SelenideElement tableSpends = $(".table.spendings-table");
    private final SelenideElement deleteSpendButton = $(".spendings__bulk-actions button");
    public final SelenideElement iconPeople = $("[data-tooltip-id=\"people\"]");

    public SelenideElement getSpend(String description) {
        return tableSpends.scrollTo()
                .$$("tr")
                .find(text(description));
    }

    public void deleteSpendingByDescription(String description) {
        getSpend(description).$("td").click();
        deleteSpendButton.click();
    }

    public void checkSpendIsEmpty() {
        tableSpends.$$("tr").shouldHave(size(0));
    }
}
