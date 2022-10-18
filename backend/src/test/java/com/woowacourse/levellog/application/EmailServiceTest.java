package com.woowacourse.levellog.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.woowacourse.levellog.common.application.EmailService;
import com.woowacourse.levellog.common.domain.Emails;
import com.woowacourse.levellog.common.support.EmailUtil;
import com.woowacourse.levellog.common.support.EmailUtilImpl;
import com.woowacourse.levellog.common.support.EnvironmentFactory;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.core.env.StandardEnvironment;

class EmailServiceTest extends ServiceTest {

    private static MockedStatic<EnvironmentFactory> environmentFactory;

    @BeforeEach
    void setUp() {
        environmentFactory = mockStatic(EnvironmentFactory.class);
    }

    @AfterEach
    void tearDown() {
        environmentFactory.close();
    }

    @DisplayName("500에러가 발생하면 profiles가 prod인 경우 Email을 발송한다.")
    @Test
    void sendByException() throws InterruptedException {
        // given
        final EmailUtil emailUtil = mock(EmailUtilImpl.class);
        final EmailService emailService = new EmailService(emailUtil);
        final StandardEnvironment environment = new StandardEnvironment();
        environment.setActiveProfiles("prod");

        given(EnvironmentFactory.get()).willReturn(environment);
        willDoNothing().given(emailUtil).sendEmail(any(), any(), any());

        // when
        emailService.sendByException(new Exception("에러 메시지"));
        Thread.sleep(1000);

        // then
        final Set<String> emails = Emails.createInstance().get();
        verify(emailUtil, times(emails.size())).sendEmail(any(), any(), any());
    }
}
