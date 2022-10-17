package com.woowacourse.levellog.common.support;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtilImpl implements EmailUtil {

    private final JavaMailSender sender;

    public EmailUtilImpl(final JavaMailSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendEmail(final String to, final String subject, final String content) {
        final MimeMessage message = sender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content);
        } catch (final MessagingException e) {
            e.printStackTrace();
        }

        sender.send(message);
    }
}
