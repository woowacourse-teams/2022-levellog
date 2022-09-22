package com.woowacourse.levellog.acceptance;

import static com.woowacourse.levellog.fixture.MemberFixture.PEPPER;
import static com.woowacourse.levellog.fixture.MemberFixture.ROMA;
import static com.woowacourse.levellog.fixture.RestAssuredTemplate.delete;
import static com.woowacourse.levellog.fixture.RestAssuredTemplate.post;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import com.woowacourse.levellog.fixture.MemberFixture;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@Nested
@DisplayName("인터뷰 검색 관련 기능")
class InterviewQuestionSearchAcceptanceTest extends AcceptanceTest {

    @Nested
    @DisplayName("인터뷰 검색")
    class SearchBy {

        /*
         * Scenario: 인터뷰 검색(비로그인)
         *   when: 인터뷰 검색을 한다.
         *   then: 200 OK 상태 코드와 결과를 응답받는다.
         */
        @Test
        @DisplayName("로그인하지 않고 조회")
        void searchBy_nonLogin() {
            // given
            PEPPER.save();
            ROMA.save();

            final String teamId = saveTeam("잠실 제이슨조", PEPPER, 1, PEPPER, ROMA).getTeamId();
            final String levellogId = saveLevellog("페퍼의 레벨로그", teamId, PEPPER).getLevellogId();

            timeStandard.setInProgress();

            saveInterviewQuestion("Spring을 사용한 이유", levellogId, ROMA);
            saveInterviewQuestion("Spring이 무엇인가요?", levellogId, ROMA);
            saveInterviewQuestion("Java가 무엇인가요?", levellogId, ROMA);

            // when
            final ValidatableResponse response = RestAssured.given(specification).log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .filter(document("interview-question-search/search-non-login"))
                    .when()
                    .get("/api/interview-questions?keyword=Spring")
                    .then().log().all();

            // then
            response.statusCode(HttpStatus.OK.value())
                    .body("results", hasSize(2));
        }

        /*
         * Scenario: 인터뷰 검색(로그인)
         *   when: 토큰을 전송하여 인터뷰 검색을 한다.
         *   then: 200 OK 상태 코드와 결과를 응답받는다.
         */
        @Test
        @DisplayName("로그인하고 조회")
        void searchBy_login() {
            // given
            PEPPER.save();
            ROMA.save();

            final String teamId = saveTeam("잠실 제이슨조", PEPPER, 1, PEPPER, ROMA).getTeamId();
            final String levellogId = saveLevellog("페퍼의 레벨로그", teamId, PEPPER).getLevellogId();

            timeStandard.setInProgress();

            saveInterviewQuestion("Spring을 사용한 이유", levellogId, ROMA);
            saveInterviewQuestion("Spring이 무엇인가요?", levellogId, ROMA);
            saveInterviewQuestion("Java가 무엇인가요?", levellogId, ROMA);

            // when
            final ValidatableResponse response = RestAssured.given(specification).log().all()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + ROMA.getToken())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .filter(document("interview-question-search/search-login"))
                    .when()
                    .get("/api/interview-questions?keyword=Spring")
                    .then().log().all();

            // then
            response.statusCode(HttpStatus.OK.value())
                    .body("results", hasSize(2));
        }

        /*
         * Scenario: 인터뷰 검색 최신 순 정렬
         *   when: 최신 순으로 정렬하는 쿼리 파라미터를 추가하여 인터뷰 검색을 한다.
         *   then: 200 OK 상태 코드와 최신 순으로 정렬된 결과를 응답받는다.
         */
        @Test
        @DisplayName("최신 순 정렬 조회")
        void searchBy_sortByUpdatedAtDesc() {
            // given
            PEPPER.save();
            ROMA.save();

            final String teamId = saveTeam("잠실 제이슨조", PEPPER, 1, PEPPER, ROMA).getTeamId();
            final String levellogId = saveLevellog("페퍼의 레벨로그", teamId, PEPPER).getLevellogId();

            timeStandard.setInProgress();

            saveInterviewQuestion("Spring을 사용한 이유", levellogId, ROMA);
            saveInterviewQuestion("Spring이 무엇인가요?", levellogId, ROMA);
            saveInterviewQuestion("Java가 무엇인가요?", levellogId, ROMA);

            // when
            final ValidatableResponse response = RestAssured.given(specification).log().all()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + ROMA.getToken())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .filter(document("interview-question-search/search-sort-updated-at-desc"))
                    .when()
                    .get("/api/interview-questions?keyword=Spring&sort=latest")
                    .then().log().all();

            // then
            response.statusCode(HttpStatus.OK.value())
                    .body("results.id", Matchers.contains(2, 1));
        }

        /*
         * Scenario: 인터뷰 검색 오래된 순 정렬
         *   when: 오래된 순으로 정렬하는 쿼리 파라미터를 추가하여 인터뷰 검색을 한다.
         *   then: 200 OK 상태 코드와 오래된 순으로 정렬된 결과를 응답받는다.
         */
        @Test
        @DisplayName("오래된 순 정렬 조회")
        void searchBy_sortByUpdatedAtAsc() {
            // given
            PEPPER.save();
            ROMA.save();

            final String teamId = saveTeam("잠실 제이슨조", PEPPER, 1, PEPPER, ROMA).getTeamId();
            final String levellogId = saveLevellog("페퍼의 레벨로그", teamId, PEPPER).getLevellogId();

            timeStandard.setInProgress();

            saveInterviewQuestion("Spring을 사용한 이유", levellogId, ROMA);
            saveInterviewQuestion("Spring이 무엇인가요?", levellogId, ROMA);
            saveInterviewQuestion("Java가 무엇인가요?", levellogId, ROMA);

            // when
            final ValidatableResponse response = RestAssured.given(specification).log().all()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + ROMA.getToken())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .filter(document("interview-question-search/search-sort-updated-at-asc"))
                    .when()
                    .get("/api/interview-questions?keyword=Spring&sort=oldest")
                    .then().log().all();

            // then
            response.statusCode(HttpStatus.OK.value())
                    .body("results.id", Matchers.contains(1, 2));
        }

        /*
         * Scenario: 인터뷰 검색 좋아요 순 정렬
         *   when: 좋아요 순으로 정렬하는 쿼리 파라미터를 추가하여 인터뷰 검색을 한다.
         *   then: 200 OK 상태 코드와 좋아요 순으로 정렬된 결과를 응답받는다.
         */
        @Test
        @DisplayName("좋아요 순 정렬 조회")
        void searchBy_sortByLikeDesc() {
            // given
            PEPPER.save();
            ROMA.save();

            final String teamId = saveTeam("잠실 제이슨조", PEPPER, 1, PEPPER, ROMA).getTeamId();
            final String levellogId = saveLevellog("페퍼의 레벨로그", teamId, PEPPER).getLevellogId();

            timeStandard.setInProgress();

            final String interviewQuestionId = saveInterviewQuestion("Spring을 사용한 이유", levellogId, ROMA)
                    .getInterviewQuestionId();
            saveInterviewQuestion("Spring이 무엇인가요?", levellogId, ROMA);
            saveInterviewQuestion("Java가 무엇인가요?", levellogId, ROMA);

            requestPressLikeInterviewQuestion(interviewQuestionId, ROMA);

            // when
            final ValidatableResponse response = RestAssured.given(specification).log().all()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + ROMA.getToken())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .filter(document("interview-question-search/search-sort-likes-desc"))
                    .when()
                    .get("/api/interview-questions?keyword=Spring&sort=likes")
                    .then().log().all();

            // then
            response.statusCode(HttpStatus.OK.value())
                    .body("results.id", Matchers.contains(1, 2))
                    .body("results.likeCount", contains(1, 0));
        }

        /*
         * Scenario: keyword 파라미터가 없이 조회
         *   when: keyword 파라미터가 없이 인터뷰 검색을 한다.
         *   then: 200 OK 상태 코드와 빈 결과를 응답받는다.
         */
        @Test
        @DisplayName("keyword 파라미터가 없이 조회")
        void searchBy_inputPercent() {
            // given
            PEPPER.save();
            ROMA.save();

            final String teamId = saveTeam("잠실 제이슨조", PEPPER, 1, PEPPER, ROMA).getTeamId();
            final String levellogId = saveLevellog("페퍼의 레벨로그", teamId, PEPPER).getLevellogId();

            timeStandard.setInProgress();

            saveInterviewQuestion("Spring을 사용한 이유", levellogId, ROMA);
            saveInterviewQuestion("Spring이 무엇인가요?", levellogId, ROMA);
            saveInterviewQuestion("Java가 무엇인가요?", levellogId, ROMA);

            // when
            final ValidatableResponse response = RestAssured.given(specification).log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/api/interview-questions")
                    .then().log().all();

            // then
            response.statusCode(HttpStatus.OK.value())
                    .body("results", hasSize(0));
        }
    }

    @Nested
    @DisplayName("좋아요 관련")
    class Like {

        /*
         * Scenario: 좋아요를 누른다
         *   when: 인터뷰 질문을 좋아요한다.
         *   then: 204 no content 상태 코드를 응답받는다.
         */
        @Test
        @DisplayName("좋아요를 누른다")
        void pressLike() {
            // given
            PEPPER.save();
            ROMA.save();

            final String teamId = saveTeam("잠실 제이슨조", PEPPER, 1, PEPPER, ROMA).getTeamId();
            final String levellogId = saveLevellog("페퍼의 레벨로그", teamId, PEPPER).getLevellogId();

            timeStandard.setInProgress();

            final String interviewQuestionId = saveInterviewQuestion("Spring을 사용한 이유", levellogId, ROMA)
                    .getInterviewQuestionId();
            saveInterviewQuestion("Spring이 무엇인가요?", levellogId, ROMA);
            saveInterviewQuestion("Java가 무엇인가요?", levellogId, ROMA);

            // when
            final ValidatableResponse response = RestAssured.given(specification).log().all()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + ROMA.getToken())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .filter(document("interview-question-search/press-like"))
                    .when()
                    .post("/api/interview-questions/{interviewQuestionId}/like", interviewQuestionId)
                    .then().log().all();

            // then
            response.statusCode(HttpStatus.NO_CONTENT.value());
        }

        /*
         * Scenario: 좋아요를 누른다
         *   when: 인터뷰 질문을 좋아요한다.
         *   then: 204 no content 상태 코드를 응답받는다.
         */
        @Test
        @DisplayName("좋아요를 취소한다")
        void cancelLike() {
            // given
            PEPPER.save();
            ROMA.save();

            final String teamId = saveTeam("잠실 제이슨조", PEPPER, 1, PEPPER, ROMA).getTeamId();
            final String levellogId = saveLevellog("페퍼의 레벨로그", teamId, PEPPER).getLevellogId();

            timeStandard.setInProgress();

            final String interviewQuestionId = saveInterviewQuestion("Spring을 사용한 이유", levellogId, ROMA)
                    .getInterviewQuestionId();
            saveInterviewQuestion("Spring이 무엇인가요?", levellogId, ROMA);
            saveInterviewQuestion("Java가 무엇인가요?", levellogId, ROMA);

            requestPressLikeInterviewQuestion(interviewQuestionId, ROMA);

            // when
            final ValidatableResponse response = RestAssured.given(specification).log().all()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + ROMA.getToken())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .filter(document("interview-question-search/cancel-like"))
                    .when()
                    .delete("/api/interview-questions/{interviewQuestionId}/like", interviewQuestionId)
                    .then().log().all();

            // then
            response.statusCode(HttpStatus.NO_CONTENT.value());
        }
    }

    private ValidatableResponse requestPressLikeInterviewQuestion(final String interviewQuestionId,
                                                                  final MemberFixture from) {
        return post("/api/interview-questions/" + interviewQuestionId + "/like", from.getToken())
                .getResponse();
    }

    private ValidatableResponse requestCancelLikeInterviewQuestion(final String interviewQuestionId,
                                                                   final MemberFixture from) {
        return delete("/api/interview-questions/" + interviewQuestionId + "/like", from.getToken())
                .getResponse();
    }
}
