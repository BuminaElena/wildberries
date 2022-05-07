package t1.steps;

import com.codeborne.selenide.Configuration;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.ru.Тогда;
import t1.pages.WildberriesMainPage;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;

public class WildberriesSteps {
    WildberriesMainPage wildberriesMainPage = new WildberriesMainPage();

    @Before
    public void setUp() {
        Configuration.baseUrl = "https://www.wildberries.ru/";
        Configuration.timeout = 10000;
        open("");
    }

    @After
    public void tearDown() {
        closeWebDriver();
    }

    @Тогда("переходим в каталог")
    public void goToCatalog() throws InterruptedException {
        wildberriesMainPage.goToCatalog();
    }

    @Тогда("переходим в меню {string} подраздел {string}")
    public void selectMenu(String menuItem, String menuSubitem) {
        wildberriesMainPage.selectMenu(menuItem, menuSubitem);
    }

    @Тогда("устанавливаем фильтр по {string} бренду {string}")
    public void filterBrend(String randomOrNot, String brandName) throws InterruptedException {
        Thread.sleep(5000);
        wildberriesMainPage.filterBrand(randomOrNot, brandName);
    }

    @Тогда("проверяем соответствие результата поиска бренду")
    public void compareResultSetWithBrand() throws InterruptedException {
        Thread.sleep(5000);
        wildberriesMainPage.compareResultSetWithBrand();
    }

    @Тогда("проверяем, что отображается результат поиска")
    public void checkResultSetIsNull() {
        wildberriesMainPage.checkResultSetIsNotNull();
    }

    @Тогда("сортируем по {string} по {string}")
    public void sortingOn(String sortingMethod, String upOrDown) throws InterruptedException {
        boolean up = (upOrDown.equals("возрастанию")) ? true : false;
        wildberriesMainPage.sortingOn(sortingMethod, up);
    }

    @Тогда("проверяем сортировку по цене по {string}")
    public void checkSortingByPrice(String upOrDown) {
        boolean up = (upOrDown.equals("возрастанию")) ? true : false;
        wildberriesMainPage.checkSortingByPrice(up);
    }

    @Тогда("добавляем произвольный товар в корзину и сохраняем его бренд, название и цену в переменные")
    public void addRandomToBasket() {
        wildberriesMainPage.addRandomToBasket();
    }

    @Тогда("отладка")
    public void debugging() throws InterruptedException {
        Thread.sleep(5000);
    }

    @Тогда("переходим в корзину")
    public void goToBasket() {
        wildberriesMainPage.goToBasket();
    }

    @Тогда("проверяем соответствие данных в корзине")
    public void checkBasket() {
        wildberriesMainPage.checkBasket();
    }

    @Тогда("удаляем товар из корзины")
    public void deleteFromBasket() throws InterruptedException {
        wildberriesMainPage.deleteFromBasket();
    }

    @Тогда("ищем {string}существующий товар {string} и проверяем результат поиска")
    public void searchByProduct(String existOrNot, String name) throws InterruptedException {
        boolean isExist = (existOrNot.equals("")) ? true : false;
        wildberriesMainPage.searchByProduct(name,isExist);
    }

    @Тогда("вводим в поле 'Цена до' значение {string}")
    public void setMaxPrice(String price) throws InterruptedException {
        wildberriesMainPage.setMaxPrice(price);
    }

    @Тогда("проверяем, что максимальная цена на 200 выше минимальной")
    public void checkMaxPrice() {
        wildberriesMainPage.checkMaxPrice();
    }

    @Тогда("проверяем, что максимальная цена не изменилась")
    public void checkMaxPriceTheSame() {
        wildberriesMainPage.checkMaxPriceTheSame();
    }

}
