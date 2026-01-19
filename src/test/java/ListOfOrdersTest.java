import general.Config;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Test;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

public class ListOfOrdersTest {

    @Test
    @DisplayName("Список заказов")
    @Description("Проверь, что в тело ответа возвращается список заказов")
    public void verifyOrdersListResponseStructure() {
        // Шаг 1: Получить список заказов
        Response response = getOrdersListStep();

        // Шаг 2: Проверить структуру ответа
        verifyResponseStructureStep(response);
    }

    @Step("Получить список заказов")
    private Response getOrdersListStep() {
        return given()
                .baseUri(Config.BASE_URL)
                .log().all()
                .when()
                .get(Config.LIST_OF_ORDERS);
    }

    @Step("Проверить структуру ответа")
    private void verifyResponseStructureStep(Response response) {
        // Основная проверка: в ответе есть поле "orders" со списком
        response.then()
                .statusCode(200)

                // Главная проверка по заданию
                .body("orders", notNullValue())

                // Проверяем структуру корневых полей
                .body("pageInfo", notNullValue())
                .body("availableStations", notNullValue())

                // Проверяем что orders - это массив
                .body("orders", instanceOf(java.util.List.class))

                // Проверяем структуру pageInfo
                .body("pageInfo.page", notNullValue())
                .body("pageInfo.total", notNullValue())
                .body("pageInfo.limit", notNullValue())

                // Проверяем что availableStations - это массив
                .body("availableStations", instanceOf(java.util.List.class));

        System.out.println("Структура ответа корректна:");
        System.out.println("  - Поле 'orders' присутствует и является списком");
        System.out.println("  - Поле 'pageInfo' присутствует с полями page, total, limit");
        System.out.println("  - Поле 'availableStations' присутствует и является списком");
    }
}