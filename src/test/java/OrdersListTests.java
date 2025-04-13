import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class OrdersListTests {
    @Test
    @DisplayName("Список заказов")
    public void getListOrdersTest() {
        given().baseUri("https://qa-scooter.praktikum-services.ru/").
                header("Content-type", "application/json")
                .log()
                .all()
                .get("/api/v1/orders")
                .then()
                .assertThat()
                .statusCode(200);
    }
}

