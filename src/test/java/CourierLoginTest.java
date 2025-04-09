import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import ru.services.practicum.Courier;
import ru.services.practicum.CourierClient;

public class CourierLoginTest {
    private final CourierClient courierClient = new CourierClient();
    private String login;
    private String password;
    private String firstName;

    @Before
    public void setUp() {
        this.login = RandomStringUtils.randomAlphanumeric(2, 15);
        this.password = RandomStringUtils.randomAlphanumeric(7, 15);
        this.firstName = RandomStringUtils.randomAlphabetic(2, 18);
    }

    @Test
    @DisplayName("Курьер авторизирован")
    public void checkCreatingCourierLoginTest() {
        Response postRequestCourierLogin = courierClient.loginCourier(new Courier("RandomName", "7880", "raandom name"));
        postRequestCourierLogin.then().log().all().assertThat().statusCode(200).and().body("id", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Курьер авторизирован без логина")
    public void checkVerificationWithoutLoginAuthorization() {
        Response postRequestCourierLogin = courierClient.loginCourier(new Courier(null, password, firstName));
        postRequestCourierLogin.then().log().all().assertThat().statusCode(400).and().body("message", Matchers.is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Курьер авторизирован без пароля")
    public void checkVerificationWithoutPasswordAuthorization() {
        Response postRequestCourierLogin = courierClient.loginCourier(new Courier(login, "", null));
        postRequestCourierLogin.then().log().all().assertThat().statusCode(400).and().body("message", Matchers.is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Курьер авторизирован под несуществующим логином")
    public void checkAuthorizationUnderIncorrectLogin() {
        Response postRequestCourierLogin = courierClient.loginCourier(new Courier("www", "212506", null));
        postRequestCourierLogin.then().log().all().assertThat().statusCode(404).and().body("message", Matchers.is("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Курьер авторизирован под некорректным логином")
    public void checkEnteringInvalidLogin() {
        Response postRequestCourierLogin = courierClient.loginCourier(new Courier("sqdfwgh", "212506", null));
        postRequestCourierLogin.then().log().all().assertThat().statusCode(404).and().body("message", Matchers.is("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Курьер авторизирован под некорректным паролем")
    public void checkEnteringInvalidPassword() {
        Response postRequestCourierLogin = courierClient.loginCourier(new Courier("RandomName", "7881", null));
        postRequestCourierLogin.then().log().all().assertThat().statusCode(404).and().body("message", Matchers.is("Учетная запись не найдена"));
    }
}

