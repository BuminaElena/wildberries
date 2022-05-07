package t1.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import t1.steps.WildberriesSteps;

import static com.codeborne.selenide.ClickOptions.usingJavaScript;
import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class WildberriesMainPage {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(WildberriesSteps.class);

    private static final SelenideElement CATALOG = $x("//button[@aria-label='Навигация по сайту']");
    private static final SelenideElement MENU_BURGER_LIST = $(".menu-burger__main-list");
    private static final ElementsCollection BRAND_LIST = $$x("//div[@data-filter-name='fbrand']//label[contains(@class,'j-list-item filter__item filter__item--checkbox')]");
    private static final SelenideElement BRAND_INPUT = $x("//div[@data-filter-name='fbrand']//input");
    private static final ElementsCollection BRAND_RESULT_OF_SEARCH_COLLECTION = $$(".brand-name");
    private static final ElementsCollection PRICE_RESULT_OF_SEARCH_COLLECTION = $$(".lower-price");
    private static final ElementsCollection NAME_RESULT_OF_SEARCH_COLLECTION = $$(".goods-name");
    private static final ElementsCollection TO_BASKET = $$x("//div[@class='product-card__order']/a");
    private static final SelenideElement BASKET = $x("//span[@class='navbar-pc__icon navbar-pc__icon--basket']/parent::a");
    private static final SelenideElement PRICE_IN_BASKET = $(".list-item__price-new");
    private static final SelenideElement BRAND_IN_BASKET = $(".good-info__good-brand");
    private static final SelenideElement NAME_IN_BASKET = $(".good-info__good-name");
    private static final SelenideElement QUANTITY_IN_BASKET = $(".navbar-pc__notify");
    private static final SelenideElement DELETE_FROM_BASKET = $x("//span[normalize-space()='Удалить']");
    private static final SelenideElement SEARCH_INPUT = $("#searchInput");
    private static final SelenideElement INPUT_PRICE_FROM = $x("//input[@class='j-price c-input-base-sm' and @name='startN']");
    private static final SelenideElement INPUT_PRICE_TO = $("input.j-price.c-input-base-sm[name='endN']");

    String brand = "", name = "", price = "", maxPrice = "";
    int quantityInBasket = 0;

    public void goToCatalog() throws InterruptedException {
        Thread.sleep(1000);
        CATALOG.scrollTo().click();
        MENU_BURGER_LIST.shouldBe(visible);
        logger.info("Перешли в каталог");
    }

    public void selectMenu(String menuItem, String menuSubitem) {
        $x("//*[text()='" + menuItem + "']").click();
        logger.info("Перешли по меню " + menuItem);
        $x("//a[@class='j-menu-item' and text()='" + menuSubitem + "']").click();
        logger.info("Перешли по меню " + menuSubitem);
        $x("//span[text() = '" + menuSubitem + "']").shouldBe(visible);
    }

    public void filterBrand(String randomOrNot, String brandName) {
        boolean random = (randomOrNot.equals("случайному")) ? true : false;
        BRAND_LIST.get(0).shouldBe(visible);
        if (random) {
            int x = BRAND_LIST.size();
            logger.info("Всего брендов " + x);
            int i = (int) (Math.random() * x);
            logger.info("Случайное число " + i);
            // Получаем название бренда и убираем из строки количество товаров в скобках
            String brand_with_quantity = BRAND_LIST.get(i).getText();
            brand = brand_with_quantity.substring(0, brand_with_quantity.lastIndexOf("(") - 1);
            BRAND_LIST.get(i).click();
            BRAND_LIST.get(i).shouldHave(attribute("class", "j-list-item filter__item filter__item--checkbox selected"));
            logger.info("Выбрали бренд " + brand);
        } else {
            BRAND_INPUT.setValue(brandName).pressEnter();
        }
    }

    public void compareResultSetWithBrand() {
        logger.info("Всего нашли товаров " + BRAND_RESULT_OF_SEARCH_COLLECTION.size());
        // Проверяем только на одной странице
        for (SelenideElement result : BRAND_RESULT_OF_SEARCH_COLLECTION) {
            // Убираем слэш в конце названия брэнда в найденных товарах
            String resultBrand = result.getText().substring(0, result.getText().length() - 2).trim();
            logger.info("Бренд элемента " + resultBrand);
            Assert.assertTrue(resultBrand.equals(brand));
        }
    }

    public void checkResultSetIsNotNull() {
        Assert.assertTrue(NAME_RESULT_OF_SEARCH_COLLECTION.size() != 0);
    }

    public void sortingOn(String sortingMethod, boolean up) throws InterruptedException {
        $x("//a[contains(@class,'sort-item') and normalize-space()='" + sortingMethod + "']").click();
        String priceUpOrDown = $x("//a[contains(@class,'sort-item') and normalize-space()='" + sortingMethod + "']/span").getAttribute("class");
        Assert.assertTrue(priceUpOrDown.equals("icon-up"));
        if (!up) {
            $x("//a[contains(@class,'sort-item') and normalize-space()='" + sortingMethod + "']").click();
            priceUpOrDown = $x("//a[contains(@class,'sort-item') and normalize-space()='" + sortingMethod + "']/span").getAttribute("class");
            Assert.assertTrue(priceUpOrDown.equals("icon-down"));
        }
        logger.info("Отсортировали по " + sortingMethod + ", " + up);
        Thread.sleep(1000);
    }

    public void checkSortingByPrice(boolean up) {
        int minPrice = 0, maxPrice = Integer.MAX_VALUE;
        for (SelenideElement price : PRICE_RESULT_OF_SEARCH_COLLECTION) {
            // Убираем нецифровые символы в цене
            int resultPrice = Integer.parseInt(price.getText().replaceAll("\\D", ""));
            logger.info("Цена элемента " + resultPrice);
            if (up) {
                Assert.assertTrue(resultPrice >= minPrice);
                minPrice = resultPrice;
            } else {
                Assert.assertTrue(resultPrice <= maxPrice);
                maxPrice = resultPrice;
            }
        }
    }

    public void addRandomToBasket() {
        BRAND_RESULT_OF_SEARCH_COLLECTION.get(0).shouldBe(visible);
        int x = BRAND_RESULT_OF_SEARCH_COLLECTION.size();
        logger.info("Найдено товаров " + x);
        int randomNumber = (int) (Math.random() * x);
        logger.info("Случайный товар " + randomNumber);
        String brand_ = BRAND_RESULT_OF_SEARCH_COLLECTION.get(randomNumber).getText();
        brand = brand_.substring(0, brand_.length() - 2);
        name = NAME_RESULT_OF_SEARCH_COLLECTION.get(randomNumber).getText();
        price = PRICE_RESULT_OF_SEARCH_COLLECTION.get(randomNumber).getText();
        logger.info("Выбрали товар: бренд " + brand + ", название " + name + ", цена " + price);
        BRAND_RESULT_OF_SEARCH_COLLECTION.get(randomNumber).hover();
        TO_BASKET.get(randomNumber).click();
        TO_BASKET.get(randomNumber).shouldHave(text("В корзине"));
        quantityInBasket++;
        logger.info("Переменная quantityInBasket = " + quantityInBasket);
    }

    public void goToBasket() {
        BASKET.click();
    }

    public void checkBasket() {
        NAME_IN_BASKET.shouldBe(visible);
        String nameInBasket = NAME_IN_BASKET.getText().substring(0, NAME_IN_BASKET.getText().length() - 1);
        logger.info("В корзине: бренд " + BRAND_IN_BASKET.getText() + ", наименование " + nameInBasket + ", цена " + PRICE_IN_BASKET.getText());
//        Не проверяем совпадение названия, т.к. может не совпадать
//        Assert.assertTrue(name.equals(nameInBasket));
        Assert.assertTrue(brand.equals(BRAND_IN_BASKET.getText()));
        Assert.assertTrue(price.equals(PRICE_IN_BASKET.getText()));
        Assert.assertTrue(Integer.parseInt(QUANTITY_IN_BASKET.getText()) == quantityInBasket);

    }

    public void deleteFromBasket() throws InterruptedException {
        BRAND_IN_BASKET.hover();
        DELETE_FROM_BASKET.click();
        Thread.sleep(1000); //ждем, когда пересчитается количество
        logger.info("Количество в корзине после удаления " + QUANTITY_IN_BASKET.getText());
        Assert.assertTrue(Integer.parseInt(QUANTITY_IN_BASKET.getText()) == quantityInBasket - 1);
        quantityInBasket--;
    }

    public void searchByProduct(String name, boolean isExist) {
        // isExist: true = существующий товар, false = несуществующий
        SEARCH_INPUT.setValue(name).pressEnter();
        if (isExist) {
            NAME_RESULT_OF_SEARCH_COLLECTION.shouldBe(sizeGreaterThan(0));
        } else Assert.assertTrue(NAME_RESULT_OF_SEARCH_COLLECTION.size() == 0);
    }

    public void setMaxPrice(String price) throws InterruptedException {
        maxPrice = INPUT_PRICE_TO.getAttribute("value");
        logger.info("Переменная maxPrice была = " + maxPrice);
        INPUT_PRICE_TO.doubleClick().sendKeys(price);
        INPUT_PRICE_TO.pressEnter();
        logger.info("Ввели максимальную цену " + price);
    }

    public void checkMaxPrice() {
        int minPrice = Integer.parseInt(INPUT_PRICE_FROM.getAttribute("value"));
        int maxPrice = Integer.parseInt(INPUT_PRICE_TO.getAttribute("value"));
        logger.info("Минимальная цена " + minPrice);
        logger.info("Максимальная цена изменилась на " + maxPrice);
        Assert.assertTrue(maxPrice == minPrice + 200);
    }

    public void checkMaxPriceTheSame() {
        logger.info("В поле Цена до значение " + INPUT_PRICE_TO.getAttribute("value"));
        Assert.assertTrue(INPUT_PRICE_TO.getAttribute("value").equals(maxPrice));
    }
}



