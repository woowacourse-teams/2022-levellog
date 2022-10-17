package com.woowacourse.levellog.common.application;

import com.woowacourse.levellog.common.domain.Emails;
import com.woowacourse.levellog.common.support.EmailUtil;
import com.woowacourse.levellog.team.domain.Team;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@Service
public class EmailService {

    private final EmailUtil emailUtil;

    public EmailService(final EmailUtil emailUtil) {
        this.emailUtil = emailUtil;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendByCreateTeam(final Team team) {
        final String subject = "[팀 생성] " + team.getDetail().getTitle();
        final String content = "생성된 팀 확인하기!" + System.lineSeparator()
                + "팀 명 : " + team.getDetail().getTitle()
                + System.lineSeparator()
                + "https://levellog.app/teams/" + team.getId();

        log.info("{} : {}", subject, team.getId());
        sendEmail(subject, content);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendByException(final Exception e) {
        final String subject = "[500 에러 발생]";
        final String content = e.getMessage() + System.lineSeparator()
                + Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.joining(System.lineSeparator()));

        sendEmail(subject, content);
    }

    private void sendEmail(final String subject, final String content) {
        final Emails emails = Emails.createInstance();
        new Thread(() ->
                emails.get().forEach(email -> emailUtil.sendEmail(email, subject, content))
        ).start();
    }
}
