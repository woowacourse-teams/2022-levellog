package com.woowacourse.levellog.fixture;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class RestAssuredTemplate {

    public static RestAssuredResponse get(final String url) {
        final ValidatableResponse response = RestAssured.given().log().all()
                .when()
                .get(url)
                .then().log().all();
        return new RestAssuredResponse(response);
    }

    public static RestAssuredResponse get(final String url, final String token) {
        final ValidatableResponse response = RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .when()
                .get(url)
                .then().log().all();
        return new RestAssuredResponse(response);
    }

    public static RestAssuredResponse post(final String url) {
        final ValidatableResponse response = RestAssured.given().log().all()
                .when()
                .post(url)
                .then().log().all();
        return new RestAssuredResponse(response);
    }

    public static RestAssuredResponse post(final String url, final String token) {
        final ValidatableResponse response = RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .when()
                .post(url)
                .then().log().all();
        return new RestAssuredResponse(response);
    }

    public static RestAssuredResponse post(final String url, final Object body) {
        final ValidatableResponse response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when()
                .post(url)
                .then().log().all();
        return new RestAssuredResponse(response);
    }

    public static RestAssuredResponse post(final String url, final String token, final Object body) {
        final ValidatableResponse response = RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when()
                .post(url)
                .then().log().all();
        return new RestAssuredResponse(response);
    }

    public static RestAssuredResponse delete(final String url) {
        final ValidatableResponse response = RestAssured.given().log().all()
                .when()
                .delete(url)
                .then().log().all();
        return new RestAssuredResponse(response);
    }

    public static RestAssuredResponse delete(final String url, final String token) {
        final ValidatableResponse response = RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .when()
                .delete(url)
                .then().log().all();
        return new RestAssuredResponse(response);
    }
}
