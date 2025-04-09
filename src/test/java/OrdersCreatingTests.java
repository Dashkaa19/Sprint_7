import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.services.practicum.Orders;

import static io.restassured.RestAssured.given;

@RunWith(Parameterized.class)
public class OrdersCreatingTests {
    private final Orders orders;

    public OrdersCreatingTests(Orders orders) {
        this.orders = orders;
    }

    @Parameterized.Parameters(name = "Создание заказа: {0}")
    public static Object[][] getTestData() {
        return new Object[][]{
                {
                        new Orders("Илья", "Петров", "ул. Ленина 1", "Чистые пруды", "89990000001", 2, "2025-04-12", "Позвонить за 10 минут", new String[]{"BLACK"})
                },
                {
                        new Orders("Мария", "Иванова", "пр. Мира 15", "ВДНХ", "89990000002", 4, "2025-04-15", "Оставить у двери", new String[]{"GREY"})
                },
                {
                        new Orders("Сергей", "Котов", "ул. Котельникова 8", "Пушкинская", "89990000003", 1, "2025-04-10", "", new String[]{"BLACK", "GREY"})
                },
                {
                        new Orders("Алена", "Зарецкая", "ул. Гагарина 33", "Цветной бульвар", "89990000004", 5, "2025-04-14", "", new String[]{})
                }
        };
    }

    @Test
    @DisplayName("Создание заказа")
    public void checkCreateOrder() {
        Response response = createOrderRequest(orders);
        response.then()
                .log().all()
                .assertThat()
                .statusCode(201)
                .body("track", Matchers.notNullValue());
    }

    private Response createOrderRequest(Orders order) {
        return given()
                .baseUri("https://qa-scooter.praktikum-services.ru/")
                .log().all()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post("/api/v1/orders");
    }
}

