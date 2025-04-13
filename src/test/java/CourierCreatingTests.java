import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.services.practicum.Courier;
import ru.services.practicum.CourierClient;

public class CourierCreatingTests {
    private final CourierClient courierClient = new CourierClient();
    private static boolean shouldRunAfter = false;
    private String login;
    private String password;
    private String firstName;

    @Before
    public void setUp() {
        this.login = RandomStringUtils.randomAlphanumeric(2, 15);
        this.password = RandomStringUtils.randomAlphanumeric(7, 15);
        this.firstName = RandomStringUtils.randomAlphabetic(2, 18);
    }

    @After
    public void tearDown() {
        if (shouldRunAfter) {
            shouldRunAfter = false;
            Response r = courierClient.loginCourier(new Courier(login, password, firstName));
            int id = r.then().extract().path("id");
            Response rs = courierClient.deleteCourierById(id);
            rs.then().log().all().assertThat().statusCode(200);
        }
    }

    @Test
    @DisplayName("Создание учетной записи курьера")
    public void createCourierTest() {
        Response postRequestCreateCourier = courierClient.createCourier(new Courier(login, password, firstName));
        postRequestCreateCourier.then().log().all().assertThat().statusCode(201).and().body("ok", Matchers.is(true));
        shouldRunAfter = true;
    }

    @Test
    @DisplayName("Создание курьера без имени курьера")
    public void creatingCourierWithoutFirstName() {
        Response postRequestCreateCourier = courierClient.createCourier(new Courier(login, password, null));
        postRequestCreateCourier.then().log().all().assertThat().statusCode(201).and().body("ok", Matchers.is(true));
        shouldRunAfter = true;
    }

    @Test
    @DisplayName("Создание курьеров с одинаковыми логинами")
    public void creatingTwoIdenticalLoginCouriers() {
        Response postRequestCreateCourier = courierClient.createCourier(new Courier("RandomName", "7880", "raandom name"));
        postRequestCreateCourier.then().log().all().assertThat().statusCode(409).and().body("message", Matchers.notNullValue());
    }


    @Test
    @DisplayName("Создание курьера без логина")
    public void creatingCourierWithoutLogin() {
        Response postRequestCreateCourier = courierClient.createCourier(new Courier(null, "1234", "egor"));
        postRequestCreateCourier.then().log().all().assertThat().statusCode(400).and().body("message", Matchers.is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    public void creatingCourierWithoutPassword() {
        Response postRequestCreateCourier = courierClient.createCourier(new Courier("ildar", null, "ildar"));
        postRequestCreateCourier.then().log().all().assertThat().statusCode(400).and().body("message", Matchers.is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без логина и пароля")
    public void creatingCourierWithoutLoginAndPassword() {
        Response postRequestCreateCourier = courierClient.createCourier(new Courier(null, null, "ildar"));
        postRequestCreateCourier.then().log().all().assertThat().statusCode(400).and().body("message", Matchers.is("Недостаточно данных для создания учетной записи"));
    }
}
