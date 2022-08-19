package com.woowacourse.levellog.acceptance;

import static com.woowacourse.levellog.fixture.MemberFixture.PEPPER;
import static com.woowacourse.levellog.fixture.MemberFixture.RICK;
import static com.woowacourse.levellog.fixture.RestAssuredTemplate.delete;
import static com.woowacourse.levellog.fixture.RestAssuredTemplate.post;
import static org.hamcrest.Matchers.notNullValue;

import com.woowacourse.levellog.admin.dto.PasswordDto;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("관리자 관련 기능")
class AdminAcceptanceTest extends AcceptanceTest {

    /*
     * Scenario: 관리자 로그인
     *   given: 비밀번호를 암호화한 hash값이 서버에 저장되어 있다.
     *   when: 사전에 전달 받은 비밀번호를 이용해 로그인을 요청한다.
     *   then: 200 OK 상태 코드와 body에 accessToken을 담아 응답받는다.
     */
    @Test
    @DisplayName("관리자 로그인")
    void login() {
        // given
        final PasswordDto request = new PasswordDto("levellog1!");

        // when
        final ValidatableResponse response = post("/admin/login", request).getResponse();

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("accessToken", notNullValue());
    }

    /*
     * Scenario: 팀 삭제
     *   given: 진행 중 인 팀이 존재한다.
     *   given: 관리자가 로그인을 한다.
     *   when: url에 토큰을 포함시켜서 삭제 요청을 한다.
     *   then: 204 상태 코드를 담아 응답받는다.
     */
    @Test
    @DisplayName("진행 중 인 팀 삭제")
    void deleteTeam() {
        // given
        PEPPER.save();
        RICK.save();

        final String teamId = saveTeam("잠실 제이슨조", PEPPER, 1, RICK).getTeamId();

        final PasswordDto request = new PasswordDto("levellog1!");
        final String token = post("/admin/login", request).getAdminToken();

        timeStandard.setInProgress();

        // when
        final ValidatableResponse response = delete("/admin/teams/" + teamId + "?token=" + token).getResponse();

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
    }
}
