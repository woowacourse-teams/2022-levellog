package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

import com.woowacourse.levellog.common.domain.Emails;
import com.woowacourse.levellog.common.support.EnvironmentFactory;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.core.env.StandardEnvironment;

class EmailsTest extends RepositoryTest {

    private static MockedStatic<EnvironmentFactory> environmentFactory;

    @BeforeEach
    void setUp() {
        environmentFactory = mockStatic(EnvironmentFactory.class);
    }

    @AfterEach
    void tearDown() {
        environmentFactory.close();
    }

    @DisplayName("Prod 서버인 경우 ADMIN 이메일 계정 Set을 반환한다.")
    @Test
    void createInstanceProd() {
        // given
        final StandardEnvironment environment = new StandardEnvironment();
        environment.setActiveProfiles("prod");

        given(EnvironmentFactory.get()).willReturn(environment);

        // when
        final Emails actual = Emails.createInstance();

        // then
        final Set<String> emails = actual.get();
        assertThat(emails).isNotEmpty();
    }

    @DisplayName("Prod 서버가 아닌 경우 빈 이메일 계정 Set을 반환한다.")
    @Test
    void createInstanceNotProd() {
        // given
        final StandardEnvironment environment = new StandardEnvironment();
        environment.setActiveProfiles("test");

        given(EnvironmentFactory.get()).willReturn(environment);

        // when
        final Emails actual = Emails.createInstance();

        // then
        assertThat(actual.get()).isEmpty();
    }
}
