package com.woowacourse.levellog.acceptance;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import com.woowacourse.levellog.authentication.dto.LoginResponse;
import com.woowacourse.levellog.dto.NicknameUpdateDto;
import com.woowacourse.levellog.fixture.RequestFixture;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpHeaders;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@Disabled
@DisplayName("MyInfo 관련 기능")
class MyInfoAcceptanceTest extends AcceptanceTest {

    /*
     * Scenario: 내 정보 조회
     *   given: 로그인이 되어있다.
     *   when: 내 정보를 조회한다.
     *   then: 로그인 된 나의 정보와 200 Ok 상태 코드를 응답받는다.
     */
    @Test
    @DisplayName("내 정보 조회")
    void readMyInfo() throws Exception {
        // given
        final LoginResponse loginResponse = RequestFixture.login("123456", "로마", "image.url");

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + loginResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("myinfo/update-nickname"))
                .when()
                .get("/api/myinfo")
                .then().log().all();

        // then
        response.body("id", Matchers.notNullValue(),
                "nickname", equalTo("로마"),
                "profileUrl", equalTo("image.url"));
    }

    /*
     * Scenario: 내 닉네임 변경하기
     *   given: 로그인이 되어있다.
     *   when: 닉네임을 변경한다.
     *   then: 닉네임이 변경되고 204 No Content 상태 코드를 응답받는다.
     */
    @Test
    @DisplayName("내 닉네임 변경하기")
    void updateMyNickname() throws Exception {
        // given
        final LoginResponse loginResponse = RequestFixture.login("123456", "로마", "image.url");
        final NicknameUpdateDto nicknameDto = new NicknameUpdateDto("새이름");

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + loginResponse.getAccessToken())
                .body(nicknameDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("myinfo/update-nickname"))
                .when()
                .put("/api/myinfo")
                .then().log().all();

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
    }
}
