package general;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class CourierClient {

    //ОБЩИЕ МЕТОДЫ ДЛЯ ТЕСТОВ С КУРЬЕРАМИ

        //Создать курьера {login}
        public static Response create(String login, String password, String firstName) {
            return given()
                    .header("Content-type", "application/json")
                    .baseUri(Config.BASE_URL)
                    .body(String.format(
                            "{\"login\": \"%s\", \"password\": \"%s\", \"firstName\": \"%s\"}",
                            login, password, firstName
                    ))
                    .when()
                    .post(Config.CREATING_A_COURIER);
        }

        //Логин курьера {login}
        public static Response login(String login, String password) {
            return given()
                    .header("Content-type", "application/json")
                    .baseUri(Config.BASE_URL)
                    .body(String.format(
                            "{\"login\": \"%s\", \"password\": \"%s\"}",
                            login, password
                    ))
                    .when()
                    .post(Config.COURIER_LOGIN);
        }

    //Удалить курьера с ID {courierId}
    public static Response delete(String courierId) {
        return given()
                .baseUri(Config.BASE_URL)
                .when()
                .delete(Config.DELETE_COURIER.replace("{id}", courierId));
    }
    }

