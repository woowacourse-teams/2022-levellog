package com.woowacourse.levellog.fixture;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class RestAssuredTemplate {

    public static ValidatableResponse get(final String url) {
        return RestAssured.given().log().all()
                .when()
                .get(url)
                .then().log().all();
    }

    public static ValidatableResponse get(final String url, final String token) {
        return RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .when()
                .get(url)
                .then().log().all();
    }

    public static ValidatableResponse post(final String url) {
        return RestAssured.given().log().all()
                .when()
                .post(url)
                .then().log().all();
    }

    public static ValidatableResponse post(final String url, final String token) {
        return RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .when()
                .post(url)
                .then().log().all();
    }

    public static ValidatableResponse post(final String url, final Object body) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when()
                .post(url)
                .then().log().all();
    }

    public static ValidatableResponse post(final String url, final String token, final Object body) {
        return RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when()
                .post(url)
                .then().log().all();
    }
}
