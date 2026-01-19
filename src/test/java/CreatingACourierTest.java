import general.Config;
import general.CourierClient;
import io.qameta.allure.junit4.DisplayName;

import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.qameta.allure.Description;
import io.qameta.allure.Step;

public class CreatingACourierTest {
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
    @DisplayName("Успешное создание курьера")
    @Description("POST запрос на создание курьера")
    @Step("Создание курьера с корректным логином и паролем")

    public void createCourierSuccessfully() {
        Response response = CourierClient.create(login, password, firstName);
        response.then()
                .log().all()
                .statusCode(201)
                .body("ok", equalTo(true));
    }


    @Test
    @DisplayName("Создание курьера без логина")
    @Description("При отправке запроса без логина возвращается 400 Bad Request")
    @Step("Создание курьера без логина")

    public void createCourierWithoutLogin() {
        Response response = CourierClient.create("", password, firstName);
        response.then()
                .log().all()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("При отправке запроса без пароля возвращается 400 Bad Request")
    @Step("Создание курьера без пароля")

    public void createCourierWithoutPassword() {
        Response response = CourierClient.create(login, "", firstName);
        response.then()
                .log().all()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

        @Test
        @DisplayName("Создание дубликата курьера")
        @Description("При создании курьера с существующим логином возвращается 409 Conflict")
        @Step("Создание курьера с уже существующим логином")

        public void createDuplicateCourier() {
            Response firstResponse = CourierClient.create(login, password, firstName);
            firstResponse.then().statusCode(201);

            // 2. Пробуем создать ТОТ ЖЕ (дубликат)
            Response duplicateResponse = CourierClient.create(login, password, firstName);
            duplicateResponse.then()
                    .log().all()
                    .statusCode(409)
                    .body("message", equalTo("Этот логин уже используется"));

        }



        @After
        public void cleanup () {
            // ПОСТУСЛОВИЕ: очистка тестовых данных
            try {
                Response loginResponse = CourierClient.login(login, password);
                if (loginResponse.statusCode() == 200) {
                    String id = loginResponse.jsonPath().get("id").toString();
                    if (id != null && !id.isEmpty() && !"null".equals(id)) {
                        CourierClient.delete(id).then().statusCode(200);
                    }
                }
                } catch(Exception e){
                }

            }
        }









