package general;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import java.time.LocalDate;

public class OrderClient {

    @Step("Создать заказ")
    public static Response createOrder(String color) {
        long timestamp = System.currentTimeMillis();

        String firstName = "Имя_" + timestamp;
        String lastName = "Фамилия_" + (timestamp % 10000);
        String address = "Адрес_" + timestamp;
        String phone = "+7999" + String.format("%07d", timestamp % 10000000);


            StringBuilder json = new StringBuilder();
            json.append("{");
            json.append("\"firstName\":\"").append(firstName).append("\",");
            json.append("\"lastName\":\"").append(lastName).append("\",");
            json.append("\"address\":\"").append(address).append("\",");
            json.append("\"metroStation\":1,");
            json.append("\"phone\":\"+79998887766\",");
            json.append("\"rentTime\":1,");
            json.append("\"deliveryDate\":\"").append(LocalDate.now().plusDays(7)).append("\",");
            json.append("\"comment\":\"\"");

            // МАССИВ color
            if (color != null && !color.isEmpty()) {
                if (color.equals("BLACK") || color.equals("GREY")) {
                    json.append(",\"color\":[\"").append(color).append("\"]");
                } else if (color.equals("BLACK,GREY")) {
                    json.append(",\"color\":[\"BLACK\",\"GREY\"]");
                }
            }
            json.append("}");

            return given()
                    .header("Content-type", "application/json")
                    .baseUri(Config.BASE_URL)
                    .body(json.toString())
                    .log().all()
                    .when()
                    .post(Config.CREATE_AN_ORDER);
        }

    //Отменить заказ
    @Step("Отменить заказ")
        public static Response cancelOrder(int track) {
            return given()
                    .baseUri(Config.BASE_URL)
                    .log().all()
                    .when()
                    .put(Config.CANCEL_ORDER.replace("{track}", String.valueOf(track)))
                    .then()
                    .extract().response();
        }
    }


