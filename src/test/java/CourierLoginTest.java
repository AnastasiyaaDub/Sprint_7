import general.Config;
import general.CourierClient;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class CourierLoginTest {

    private String login;
    private String password;
    private String firstName;

    @Before
    public void generateTestData() {
        // ПРЕДУСЛОВИЕ: сгенерировать тестовые данные
        String[] courierData = Config.generateCourierData();
        this.login = courierData[0];
        this.password = courierData[1];
        this.firstName = courierData[2];
    }

    @Test
    @DisplayName("Успешный логин курьера")
    @Description("POST запрос на логин курьера")
    @Step("Логин курьера с корректными данными")

    public void successfulCourierLogin() {
        // 1. Создаем курьера (предусловие)
        CourierClient.create(login, password, firstName).then().statusCode(201);

        // 2. Логинимся
        Response response = CourierClient.login(login, password);
        response.then()
                .log().all()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Логин с неправильным паролем")
    @Description("Неправильный пароль возвращает 404")
    @Step("Попытка залогинить курьера с неправильным паролем")
    public void loginWithWrongPassword() {
        // 1. Создаем курьера (предусловие)
        CourierClient.create(login, password, firstName).then().statusCode(201);

        // 2. Логинимся с неправильным паролем
        Response response = CourierClient.login(login, "wrong_password");
        response.then()
                .log().all()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Логин с неправильным логином")
    @Description("Неправильный логин возвращает 404")
    @Step("Попытка залогинить курьера с неправильным логином")
    public void loginWithWrongLogin() {
        // 1. Создаем курьера (предусловие)
        CourierClient.create(login, password, firstName).then().statusCode(201);

        // 2. Логинимся с неправильным логином
        Response response = CourierClient.login("wrong_login", password);
        response.then()
                .log().all()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Логин без пароля")
    @Description("Отсутствие пароля возвращает 400")
    @Step("Попытка залогинить курьера без пароля")
    public void loginWithoutPassword() {
        Response response = CourierClient.login(login, "");
        response.then()
                .log().all()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Логин без логина")
    @Description("Отсутствие логина возвращает 400")
    @Step("Попытка залогинить курьера без логина")
    public void loginWithoutLogin() {
        Response response = CourierClient.login("", password);
        response.then()
                .log().all()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Логин несуществующего курьера")
    @Description("Авторизация под несуществующим пользователем возвращает 404")
    @Step("Попытка залогинить несуществующего курьера")
    public void loginNonExistentCourier() {
        Response response = CourierClient.login("nonexistent_courier", "wrong_password");
        response.then()
                .log().all()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }


    @After  //постусловие
    public void cleanup() {
        try {
            Response loginResponse = CourierClient.login(login, password);
            if (loginResponse.statusCode() == 200) {
                String id = loginResponse.jsonPath().get("id").toString();
                if (id != null && !id.isEmpty() && !"null".equals(id)) {
                    CourierClient.delete(id).then().statusCode(200);
                }
            }
        } catch (Exception e) {

        }
    }
}
