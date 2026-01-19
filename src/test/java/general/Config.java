package general;

public class Config {

public static final String BASE_URL = "http://qa-scooter.praktikum-services.ru/";

//создание курьера
    public static final String CREATING_A_COURIER = "/api/v1/courier";
//логин курьера
    public static final String COURIER_LOGIN = "/api/v1/courier/login";
//удаление курьера
    public static final String DELETE_COURIER = "/api/v1/courier/{id}";
//создание заказа
    public static final String CREATE_AN_ORDER = "/api/v1/orders";
//получение списка заказов (10 заказов, доступных для взятия курьером)
    public static final String LIST_OF_ORDERS = "/api/v1/orders?limit=10&page=0";
//отменить заказ
    public static final String CANCEL_ORDER = "/api/v1/orders/cancel";


    //генератор тестовых данных
    public static String[] generateCourierData() {
        long timestamp = System.currentTimeMillis();
        return new String[] {
                "courier_" + timestamp,
                "pass_" + timestamp,
                "Name_" + timestamp
        };
    }
}
