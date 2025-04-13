package ru.services.practicum;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.specification.RequestSpecification;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierClient {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru/"; // пример
    private static final String CREATE_COURIER = "api/v1/courier";
    private static final String LOGIN_COURIER = "api/v1/courier/login";
    private static final String DELETE_COURIER = "api/v1/courier/";

    private RequestSpecification baseRequestSpec() {
        return given()
                .baseUri(BASE_URL)
                .filter(new AllureRestAssured())
                .header("Content-type", "application/json");
    }

    @Step("Создание курьера")
    public Response createCourier(Courier courier) {
        return baseRequestSpec()
                .body(courier)
                .when()
                .post(CREATE_COURIER);
    }

    @Step("Логин курьера")
    public Response loginCourier(Courier courier) {
        return baseRequestSpec()
                .body(courier)
                .when()
                .post(LOGIN_COURIER);
    }

    @Step("Удаление курьера по id = {courierId}")
    public Response deleteCourierById(int courierId) {
        return baseRequestSpec()
                .when()
                .delete(DELETE_COURIER + courierId);
    }
}

