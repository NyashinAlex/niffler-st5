package guru.qa.niffler.page.main;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    private final SelenideElement tableSpends = $(".table.spendings-table");
    private final SelenideElement tableNullSpends = $(".table.spendings-table tbody");
    private final SelenideElement deleteSpendButton = $(".spendings__bulk-actions button");

    public SelenideElement getSpend(String description) {
        return tableSpends.scrollTo()
                .$$("tr")
                .find(text(description));
    }

    public void deleteSpend(SelenideElement rowWithSpending) {
        rowWithSpending.$$("td").first().click();
        deleteSpendButton.click();
    }

    public void checkSpendNull() {
        tableNullSpends.$$("tr").shouldHave(size(0));
    }
}
