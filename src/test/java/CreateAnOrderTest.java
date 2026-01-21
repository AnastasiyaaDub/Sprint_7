import general.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;
import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class CreateAnOrderTest {
    private String color;
    private int track = -1;

    public CreateAnOrderTest(String color) {

        this.color = color;
    }

    @Parameterized.Parameters(name = "Цвет: {3}")
    public static Collection<Object[]> getOrderData() {
        return Arrays.asList(new Object[][]{
                {"BLACK"},
                {"GREY"},
                {"BLACK,GREY"},
                {""}
        });
    }

    @Test
    @DisplayName("Создание заказа с разными цветами")
    @Description("Проверка BLACK, GREY, BLACK+GREY, без цвета")

    public void createOrderWithDifferentColors() {
        Response response = OrderClient.createOrder(color);
        response.then()
                .log().all()
                .statusCode(201)
                .body("track", notNullValue());
        track = response.jsonPath().getInt("track");
    }

   @After
    public void cleanup() {
        if (track > 0) {
            System.out.println("Пытаемся отменить заказ с track = " + track);

            try {
                Response cancelResponse = OrderClient.cancelOrder(track);
                int statusCode = cancelResponse.statusCode();

                if (statusCode == 200) {
                    System.out.println("Заказ успешно отменен");
                } else {
                    // Логируем ошибку
                    System.err.println("ОШИБКА ОЧИСТКИ (не влияет на тест):");
                    System.err.println("  Код: " + statusCode);
                    System.err.println("  Тело: " + cancelResponse.getBody().asString());

                    // Для Allure
                    io.qameta.allure.Allure.step("Очистка не удалась (статус " + statusCode + ")",
                            () -> { /* пустой шаг */ });
                }
            } catch (Exception e) {
                System.err.println("ИСКЛЮЧЕНИЕ ПРИ ОЧИСТКЕ (игнорируется): " + e.getMessage());
            }
        }
    }
}

