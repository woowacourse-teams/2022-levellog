package com.woowacourse.levellog.presentation;

import com.woowacourse.levellog.support.ControllerTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(MyInfoController.class)
class MyInfoControllerTest extends ControllerTest {

    // FIXME : 팀 API 구현 후 수정
    @Disabled
    @Test
    void findAllMyFeedback() {
    }
}
