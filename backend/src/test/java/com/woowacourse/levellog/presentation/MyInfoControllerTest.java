package com.woowacourse.levellog.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.member.dto.request.NicknameUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("MyInfoController의")
class MyInfoControllerTest extends ControllerTest {

    @Nested
    @DisplayName("updateNickname 메서드는 ")
    class UpdateNickname {

        private static final String BASE_SNIPPET_PATH = "my-info/update/";

        @Test
        @DisplayName("닉네임에 50자를 초과한 문자열이 들어올 경우 예외를 던진다.")
        void updateNickname_nicknameInvalidLength_exception() throws Exception {
            // given
            final String message = "닉네임은 50자 이하여야합니다.";
            willThrow(new InvalidFieldException(message, DebugMessage.init()))
                    .given(memberService)
                    .updateNickname(any(), any());
            final String invalidNickname = "a".repeat(51);

            final NicknameUpdateRequest request = new NicknameUpdateRequest(invalidNickname);

            // when
            final ResultActions perform = requestUpdateNickname(request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "nickname-invalid-length"));
        }

        @ParameterizedTest
        @ValueSource(strings = {" "})
        @NullAndEmptySource
        @DisplayName("닉네임에 null 또는 빈 값이 들어온 경우 예외를 던진다.")
        void updateNickname_nicknameNullAndEmpty_exception(final String invalidNickname) throws Exception {
            // given
            final NicknameUpdateRequest request = new NicknameUpdateRequest(invalidNickname);

            // when
            final ResultActions perform = requestUpdateNickname(request);

            // then
            perform.andExpectAll(
                    status().isBadRequest()
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "nickname-blank"));
        }

        private ResultActions requestUpdateNickname(final NicknameUpdateRequest request) throws Exception {
            return requestPut("/api/my-info", request);
        }
    }
}
